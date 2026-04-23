import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl} from '@angular/forms';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {share} from 'rxjs/operators';
import {Ingredient} from '../../../models/ingredient';
import {Material} from '../../../models/material';
import {MaterialIngredient} from '../../../models/material-ingredient';
import {MaterialTag} from '../../../models/materialtag.enum';
import {Tag} from '../../../models/tag';
import {AuthService} from '../../../services/auth.service';
import {IngredientService} from '../../../services/ingredient.service';
import {MaterialsService} from '../../../services/materials.service';
import {UserService} from '../../../services/user.service';
import {MaterialFormComponent} from '../material-form/material-form.component';
import {User} from '../../../models/user';
import {SaveStatus} from '../../../models/save-status.enum';
import {MatSnackBar} from '@angular/material/snack-bar';
import {AppConfigService} from '../../../services/app-config.service';

@Component({
  selector: 'app-edit-material-form',
  templateUrl: './edit-material-form.component.html',
  styleUrls: ['./edit-material-form.component.css']
})
export class EditMaterialFormComponent extends MaterialFormComponent implements OnInit, OnDestroy {
  private readonly allowedAction = 'duplicate';
  private routeSubscription: Subscription;
  private queryParamsSubscription: Subscription;
  private isDuplicateAction = false;
  public material: Material;
  public overviewImagePreview: string;
  public closeUpImagePreview: string;
  loadingEditDone:boolean = true;
  onSubmitEdit:boolean = false;

  constructor(protected materialService: MaterialsService, protected ingredientService: IngredientService,
              protected router: Router, protected userService: UserService, protected authService: AuthService,
              protected activatedRoute: ActivatedRoute, protected snackBar: MatSnackBar, protected configService: AppConfigService) {
    super(materialService, ingredientService, router, userService, authService, snackBar, configService);
    super.ngOnInit();
  }

  ngOnInit(): void {
    this.queryParamsSubscription = this.activatedRoute.queryParams.subscribe(queryParams => {
      // Check for ?action=duplicate
      if (queryParams.action) {
        this.isDuplicateAction = queryParams.action === this.allowedAction;
      }
    });

    this.routeSubscription = this.activatedRoute.params.subscribe((params: Params) => {
      if (params.sequence_number) {
        this.materialService.getBySequenceNumber(params.sequence_number).pipe(share()).subscribe(material => {
          this.material = Material.trueCopy(material);

          // Not allowed to edit published labels. Duplicate published/draft labels are allowed
          if (this.material.getSaveStatus() === SaveStatus.PUBLISHED && !this.isDuplicateAction) {
            if (!this.authService.isAdmin()) {
              this.router.navigate(['/not-found']);
            }
          }

          this.user = User.trueCopy(this.material.getUser());

          if (this.isDuplicateAction) {
            this.userService.getUserProfile(this.authService.currentUser.getId()).subscribe(user => {
              this.user = User.trueCopy(user);
            });
          }

          // Convert json response to correct typescript model
          if (this.material.getMaterialIngredients().length > 0) {
            this.material.getMaterialIngredients().forEach(value => {
              const materialIngredient: MaterialIngredient = MaterialIngredient.trueCopy(value);
              const ingredient: Ingredient = Ingredient.trueCopy(materialIngredient.getIngredient());

              materialIngredient.setIngredient(ingredient);
              this.materialIngredients.push(materialIngredient);
            });
          }

          // Parent id needs to be to the current material sequence number if a duplicate is made. Label will be based of on that
          this.material.setParentId(this.isDuplicateAction ? this.material.getSequenceNumber() : this.material.getParentId());

          // Fill in parent info if exist
          if (this.material.getParentId()) {
            this.materialService.getBySequenceNumber(this.material.getParentId()).subscribe(parentMaterial => {
              const foundMaterial = Material.trueCopy(parentMaterial);
              const author = User.trueCopy(foundMaterial.getUser());

              this.parentId = foundMaterial.getSequenceNumber();
              this.recipeTitle = foundMaterial.getName();
              this.recipeAuthor = `By ${author.getFirstname()} ${author.getLastname()}`;

              this.materialForm.get('variationOn').setValue(true);
            });
          }

          this.setAndUpdateMaterialForm();
        }, error => {
          if (error.status === 404) {
            this.router.navigate(['/not-found']);
          }
        });
      }
    });
  }

  private setAndUpdateMaterialForm(): void {
    const titleControl: AbstractControl = this.materialForm.get('title');
    const urlControl: AbstractControl = this.materialForm.get('url');
    const typeControl: AbstractControl = this.materialForm.get('type');
    const sequenceNumberControl: AbstractControl = this.materialForm.get('sequenceNumber');
    const referenceAuthorControl: AbstractControl = this.materialForm.get('referenceAuthor');
    const referenceTitleControl: AbstractControl = this.materialForm.get('referenceTitle');
    const referenceYearControl: AbstractControl = this.materialForm.get('referenceYear');


    titleControl.setValue(this.material.getName());
    titleControl.updateValueAndValidity();

    const existingUrl = (this.material.getQRCodeURL() && this.material.getQRCodeURL() !== 'No link added')
      ? this.material.getQRCodeURL() : null;
    urlControl.setValue(existingUrl);
    this.bitlyURL = existingUrl;
    urlControl.updateValueAndValidity();

    this.material.getTags().forEach(tagValue => {
      const tag: Tag = Tag.trueCopy(tagValue);
      const materialTag: MaterialTag = MaterialTag[tag.getName()];

      this.tags.push(materialTag);
      this.tagKeys.push(tag.getName());
    });

    typeControl.setValue(this.material.getType());
    typeControl.updateValueAndValidity();

    if (this.material.getParentId()) {
      sequenceNumberControl.setValue(this.material.getParentId());
      sequenceNumberControl.updateValueAndValidity();
    } else {
      const referenceParts = this.material.getReference().split(' - ');
      referenceAuthorControl.setValue(referenceParts[0].split('By ')[1]);
      referenceTitleControl.setValue(referenceParts[1]);
      referenceYearControl.setValue(referenceParts[2]);

      referenceAuthorControl.updateValueAndValidity();
      referenceTitleControl.updateValueAndValidity();
      referenceYearControl.updateValueAndValidity();

      const referenceEmailControl: AbstractControl = this.materialForm.get('referenceEmail');
      referenceEmailControl.setValue(this.material.referenceEmail || null);
      referenceEmailControl.updateValueAndValidity();
    }

    this.materialForm.get('changes').setValue(this.material.getChanges());
    // Convert string to arr by delimiter used in db
    if (this.material.getSteps() != "No Steps added yet") {
      this.steps = this.material.getSteps().split('|');
    }
    // Restore post-processing tags
    if (this.material.getPostProcessingTags()) {
      this.selectedPostProcessingTags = this.material.getPostProcessingTags().split('|');
    }
    this.materialForm.get('status').setValue(this.material.getSaveStatus());
    this.materialForm.get('step').updateValueAndValidity();
    this.materialForm.get('ingredient').updateValueAndValidity();
    this.materialForm.get('changes').updateValueAndValidity();
    this.materialForm.get('status').updateValueAndValidity();
  }

  ngOnDestroy(): void {
    this.routeSubscription.unsubscribe();
    this.queryParamsSubscription.unsubscribe();
  }

  hasTag(tagKey: string): boolean {
    return this.tags.includes(MaterialTag[tagKey]);
  }

  public onCreateLabelPublishedEdit() {
    super.onCreateLabelPublished();
  }

  closePopup(): void {
    super.closePopup();
  }

  public onSubmit(): void {
    this.onSubmitEdit = true;
    this.loadingEditDone = false;

    if (this.isDuplicateAction) {
      // Get image from the label that was requested to duplicate
      const overviewURL = this.material.getOverviewURL() ? `data:image/png;base64,${this.material.getOverviewURL()}` : null;
      const closeUpURL = this.material.getCloseUpURL() ? `data:image/png;base64,${this.material.getCloseUpURL()}` : null;

      // Overwrite any images if no images have been uploaded with the input
      this.overviewFileUpload.mediaDataURL = this.overviewFileUpload.mediaDataURL ? this.overviewFileUpload.mediaDataURL : overviewURL;
      this.closeUpFileUpload.mediaDataURL = this.closeUpFileUpload.mediaDataURL ? this.closeUpFileUpload.mediaDataURL : closeUpURL;

      super.onSubmit();
      return;
    }


    let changes = "No changes";
    if (this.materialForm.get('changes').value != null) {
      changes = this.materialForm.get('changes').value.trim();
    }

    const author = this.materialForm.get('referenceAuthor').value?.trim();
    const refTitle = this.materialForm.get('referenceTitle').value?.trim();
    const year = this.materialForm.get('referenceYear').value;
    let reference = `By ${this.recipeAuthor} - ${this.recipeTitle} - ${this.recipeYear}`;

    if (!this.materialForm.get('variationOn').value) {
      reference = (author || refTitle || year)
        ? `By ${author || ''} - ${refTitle || ''} - ${year || ''}`
        : 'No references';
      this.parentId = null;
    }

    const tags: Tag[] = [];

    this.tagKeys.forEach(value => {
      const tag: Tag = new Tag((Object.keys(MaterialTag).indexOf(value) + 1), value);
      tags.push(tag);
    });



    const title = this.materialForm.get('title').value?.trim() || 'Untitled';

    if (this.steps.length == 0) {
      this.steps.push('No Steps added yet');
    }

    if (!this.overviewFileUpload.isValid() || !this.closeUpFileUpload.isValid()) {
      this.popupPublish = false;
      this.onSubmitEdit = false;
      this.loadingEditDone = true;
      return;
    }

    const material: Material = new Material(this.material.getSequenceNumber(), title,
      changes, this.steps.join('|'), this.bitlyURL || 'No link added', tags, this.materialIngredients, this.materialForm.get('status').value,
      this.materialForm.get('type').value, this.user, this.parentId, reference);

    material.setOverviewURL(this.overviewFileUpload.mediaDataURL ? this.overviewFileUpload.mediaDataURL : null);
    material.setCloseUpURL(this.closeUpFileUpload.mediaDataURL ? this.closeUpFileUpload.mediaDataURL : null);
    material.organisation = this.organisationName;
    material.postProcessingTags = this.selectedPostProcessingTags.join('|');
    material.referenceEmail = this.materialForm.get('referenceEmail').value?.trim() || null;
    material.setSequenceNumberPublished(this.material.getSequenceNumberPublished());

    this.materialService.update(material.getSequenceNumber(), material).subscribe(data => {
      if (this.materialForm.get('status').value === SaveStatus.PUBLISHED) {
        this.snackBar.open('Request sent to the admin', 'Close', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
        });
      }
      this.router.navigate(['material', this.material.getSequenceNumber()]);
    }, error => {
      console.log(error);
      this.snackBar.open('Something went wrong. Please try again.', 'Close', {
        duration: 7000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
      });
      this.popupPublish = false;
      this.onSubmitEdit = false;
      this.loadingEditDone = true;
    });
  }

  updateImagePreview(isOverview: boolean): void {
    // Give some time to handle the conversion
    setTimeout(() => {
      if (isOverview) {
        this.overviewImagePreview = this.overviewFileUpload.mediaDataURL;
        this.material.setOverviewURL(null);
      } else {
        this.closeUpImagePreview = this.closeUpFileUpload.mediaDataURL;
        this.material.setCloseUpURL(null);
      }
    }, 200);
  }
}
