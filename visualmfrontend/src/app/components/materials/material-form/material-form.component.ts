import {Component, ElementRef, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels} from '@techiediaries/ngx-qrcode';
import {MaterialTag} from 'src/app/models/materialtag.enum';
import {PostProcessingTag} from 'src/app/models/postprocessingtag.enum';
import {Material} from 'src/app/models/material';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {MaterialsService} from 'src/app/services/materials.service';
import {Router} from '@angular/router';
import {IngredientService} from 'src/app/services/ingredient.service';
import {Ingredient} from 'src/app/models/ingredient';
import {MaterialIngredient} from 'src/app/models/material-ingredient';
import {SaveStatus} from 'src/app/models/save-status.enum';
import {MaterialType} from 'src/app/models/material-type.enum';
import {FileUploadComponent} from '../../file-upload/file-upload.component';
import {Tag} from '../../../models/tag';
import {User} from '../../../models/user';
import {UserService} from '../../../services/user.service';
import {AuthService} from '../../../services/auth.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {AppConfigService} from '../../../services/app-config.service';
import jspdf from 'jspdf';
import html2canvas from 'html2canvas';

@Component({
  selector: 'app-material-form',
  templateUrl: './material-form.component.html',
  styleUrls: ['./material-form.component.css'],
})
export class MaterialFormComponent implements OnInit {
  public readonly maximumSteps: number = Material.MAXIMUM_STEPS;
  public readonly maximumIngredients: number = Material.MAXIMUM_INGREDIENTS;
  public recipeTitle: string;
  public recipeAuthor: string;
  public recipeYear: string;
  public searchFailed = false;
  public materialTagValues: string[];
  public materialTagKeys: string[];
  public postProcessingTagValues: string[];
  public selectedPostProcessingTags: string[];
  public materialTypeValues: string[];
  public materialTypeKeys: string[];
  public tags: MaterialTag[]; // Selected tags for view
  public tagKeys: string[]; // Selected tags to be sent towards the API endpoint
  public steps: string[];
  public materialIngredients: MaterialIngredient[];
  public creationDate: Date;
  public sequenceNumber: string;
  public creationFailed = false;
  public fileError = false;
  public saveStatus = SaveStatus;
  public user: User;
  public logoPath: string;
  public organisationName: string;
  public orgDropdownOpen = false;
  public organisations: {organisation: string, logoPath: string}[] = [];
  public labelTemplate: any = null;
  protected parentId: number = null;
  public materials: Material[] = [];
  public popupPublish:boolean = false;
  loadingDone:boolean = true;

  @ViewChild('overviewImg') overviewFileUpload: FileUploadComponent;
  @ViewChild('closeUpImg') closeUpFileUpload: FileUploadComponent;

  public elementType = NgxQrcodeElementTypes.URL;
  public correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;

  public materialForm: FormGroup;
  public bitlyURL: string;
  onSubmitDisable: boolean = false;
  public selectedFormat: string = 'a4';
  public pdfFormats: string[] = ['a4', 'a5', 'a6'];
  public materialType = MaterialType;
  public materialTag = MaterialTag;

  constructor(protected materialService: MaterialsService, protected ingredientService: IngredientService,
              protected router: Router, protected userService: UserService, protected authService: AuthService,
              protected snackBar: MatSnackBar, protected configService: AppConfigService) {
    this.tags = [];
    this.tagKeys = [];
    this.selectedPostProcessingTags = [];
    this.steps = [];
    this.materialIngredients = [];
    this.creationDate = new Date();

    this.configService.getAll().subscribe(config => {
      this.logoPath = config.logo_path;
      this.organisationName = config.organisation;
      // Load template for the default organisation
      this.configService.getTemplatesByOrganisation(config.organisation).subscribe(templates => {
        if (templates && templates.length > 0) {
          this.labelTemplate = templates[0];
        }
      });
    });

    this.configService.getOrganisations().subscribe(orgs => {
      this.organisations = orgs;
    });
  }

  ngOnInit(): void {
    const whitespaceCheck: RegExp = new RegExp('\\S');

    this.materialForm = new FormGroup({
      'title': new FormControl(null, [Validators.required, Validators.maxLength(30),
        Validators.pattern(whitespaceCheck)]),
      'url': new FormControl(null, this.validURL.bind(this)),
      'step': new FormControl(null, this.emptySteps.bind(this)),
      'changes': new FormControl(null, [Validators.pattern(whitespaceCheck), Validators.maxLength(80)]),
      'sequenceNumber': new FormControl(null),
      'variationOn': new FormControl(false),
      'referenceAuthor': new FormControl(null, [Validators.pattern(whitespaceCheck)]),
      'referenceEmail': new FormControl(null, Validators.email),
      'referenceTitle': new FormControl(null, [Validators.pattern(whitespaceCheck)]),
      'referenceYear': new FormControl(null, [Validators.pattern(new RegExp('\\d')), Validators.minLength(4)]),
      'ingredient': new FormControl(null, this.emptyIngredients.bind(this)),
      'ingredientUnit': new FormControl('gr'),
      'amount': new FormControl(null, Validators.pattern('^[0-9]*$')),
      'status': new FormControl(SaveStatus.DRAFT, Validators.required),
      'type': new FormControl(null, Validators.required)
    });

    this.setVariationOnValidators();

    this.materialTagValues = Object.values(MaterialTag);
    this.materialTagKeys = Object.keys(MaterialTag);
    this.postProcessingTagValues = Object.values(PostProcessingTag);
    this.materialTypeValues = Object.values(MaterialType);
    this.materialTypeKeys = Object.keys(MaterialType);

    this.userService.getUserProfile(this.authService.currentUser.getId()).subscribe(user => {
      this.user = Object.assign(new User(), user);
    });
  }

  private setVariationOnValidators(): void {
    this.materialForm.get('variationOn').valueChanges.subscribe(required => {
      const sequenceNumberControl: AbstractControl = this.materialForm.get('sequenceNumber');
      if (required) {
        sequenceNumberControl.setValidators([Validators.required]);
      } else {
        sequenceNumberControl.setValidators(null);
      }
      sequenceNumberControl.updateValueAndValidity();
    });
  }

  public onCreateLabelPublished() {
    this.materialForm.get('status').setValue(SaveStatus.PUBLISHED);

    if (!this.materialForm.valid) {
      this.materialForm.markAllAsTouched();

      this.snackBar.open('Oops something went wrong :( Check all the fields for errors ', 'Close', {
        duration: 20000,
        horizontalPosition: 'center', verticalPosition: 'bottom',
      });

      return;
    }

    this.popupPublish = true;
  }

  public onSaveAsPdf(): void {
    this.materialForm.get('status').setValue(SaveStatus.DRAFT);
    this.onSubmit();
  }

  closePopup(): void {
    this.popupPublish = false;
  }

  public onSubmit(): void {
    this.onSubmitDisable = true;
    const changes = this.materialForm.get('changes').value?.trim() || 'No changes';

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

    const material: Material = new Material(0, title,
      changes, this.steps.join('|'), this.bitlyURL, tags, this.materialIngredients,
      this.materialForm.get('status').value, this.materialForm.get('type').value, this.user, this.parentId, reference);

    material.setOverviewURL(this.overviewFileUpload.mediaDataURL);
    material.setCloseUpURL(this.closeUpFileUpload.mediaDataURL);
    material.organisation = this.organisationName;
    material.postProcessingTags = this.selectedPostProcessingTags.join('|');

    if (this.overviewFileUpload.isValid() && this.closeUpFileUpload.isValid()) {
      this.loadingDone = false;
      this.materialService.save(material).subscribe(data => {
        this.creationFailed = false;
        this.router.navigate(['/home']);
      }, error => {
        console.log(error);
        this.creationFailed = true;
        this.onSubmitDisable = false;
        this.loadingDone = true;
      });
    } else {
      this.fileError = true;
      this.onSubmitDisable = false;
    }
  }


  public validURL(control: FormControl): { [s: string]: boolean } {
    if (!control.value) {
      return null;
    }

    const reg = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';
    const result = control.value.match(reg);

    if (!result) {
      return {'urlIsInvalid': true};
    }

    return null;
  }

  public emptySteps(control: FormControl): { [s: string]: boolean } {
    if (this.steps.length === 0) {
      return {'stepsIsEmpty': true};
    }

    return null;
  }

  public emptyIngredients(control: FormControl): { [s: string]: boolean } {
    if (this.materialIngredients.length === 0) {
      return {'ingredientsIsEmpty': true};
    }

    return null;
  }

  public generateQRCode(): void {
    const urlControl: AbstractControl = this.materialForm.get('url');

    if (!urlControl.value || (urlControl.errors && urlControl.errors.urlIsInvalid)) {
      return;
    }

    // Try Bitly shortening, fall back to using the URL directly
    this.materialService.createBitlyLinkFromURL(urlControl.value).subscribe(
      data => {
        this.bitlyURL = data.link;
        urlControl.reset();
        urlControl.setValue(data.link);
      },
      error => {
        // Bitly not configured or failed — use the URL directly
        this.bitlyURL = urlControl.value;
        urlControl.updateValueAndValidity();
      }
    );
  }

  public updateTags(event: any, data: string): void {
    if (event.target.checked) {
      this.tags.push(MaterialTag[data]);
      this.tagKeys.push(data);
    } else {
      const removeIndexValues = this.tags.findIndex(item => item === MaterialTag[data]);
      const removeIndexKeys = this.tagKeys.findIndex(item => item === data);

      // Make sure a tag has been found in the array
      if (removeIndexValues !== -1) {
        this.tags.splice(removeIndexValues, 1);
      }

      if (removeIndexKeys !== -1) {
        this.tagKeys.splice(removeIndexKeys, 1);
      }
    }
  }

  public updatePostProcessingTags(event: any, value: string): void {
    if (event.target.checked) {
      this.selectedPostProcessingTags.push(value);
    } else {
      const idx = this.selectedPostProcessingTags.indexOf(value);
      if (idx !== -1) {
        this.selectedPostProcessingTags.splice(idx, 1);
      }
    }
  }

  public addStep(): void {
    const stepControl: AbstractControl = this.materialForm.get('step');

    if (stepControl.value && this.steps.length < this.maximumSteps) {
      this.steps.push(stepControl.value.trim());
      stepControl.reset();
    }
  }

  public addIngredient(): void {
    const ingredientControl: AbstractControl = this.materialForm.get('ingredient');
    const amountControl: AbstractControl = this.materialForm.get('amount');
    const ingredientUnitControl: AbstractControl = this.materialForm.get('ingredientUnit');

    const unitValue = ingredientUnitControl.value || 'gr';

    const selectedIngredient = new Ingredient(0, ingredientControl.value.trim(), unitValue);
    // Add ingredient when value has been set
    if (selectedIngredient != null && ingredientControl.value && amountControl.value
      && this.materialIngredients.length < this.maximumIngredients) {
      const materialIngredient: MaterialIngredient = new MaterialIngredient(selectedIngredient, amountControl.value);

      this.materialIngredients.push(materialIngredient);

      ingredientControl.reset();
      amountControl.reset();
      ingredientUnitControl.setValue('gr');
    }
  }

  public removeFromList(data: string): void {
    const removeIndex = this.steps.findIndex(item => item === data);

    if (removeIndex !== -1) {
      this.steps.splice(removeIndex, 1);
    }

    this.materialForm.get('step').reset();
  }

  public removeFromMaterialIngredients(ingredient: Ingredient): void {
    const removeIndex = this.materialIngredients.findIndex(item => item.getIngredient() === ingredient);

    if (removeIndex !== -1) {
      this.materialIngredients.splice(removeIndex, 1);
    }
  }

  public onSelectFormat(value: string): void {
    this.selectedFormat = value;
  }

  get displayLogoPath(): string {
    return this.logoPath;
  }

  public onOrgSelected(org: {organisation: string, logoPath: string}): void {
    this.organisationName = org.organisation;
    this.logoPath = org.logoPath;
    this.orgDropdownOpen = false;
    this.configService.getTemplatesByOrganisation(org.organisation).subscribe(templates => {
      this.labelTemplate = (templates && templates.length > 0) ? templates[0] : null;
    });
  }

  public saveOrganisationName(): void {
    this.orgDropdownOpen = false;
  }

  public generatePdf(): void {
    const title = this.materialForm.get('title').value || 'Untitled';
    const data = window.document.getElementById('pdfLabelForm');
    data.style.display = 'block';

    html2canvas(data, { scale: 2 }).then(canvas => {
      data.style.display = 'none';
      const contentDataURL = canvas.toDataURL('image/png');
      const format = this.selectedFormat || 'a4';
      const pdf = new jspdf('p', 'mm', format);
      pdf.addImage(contentDataURL, 'PNG', 0, 0, pdf.internal.pageSize.getWidth(), pdf.internal.pageSize.getHeight());

      if (format === 'a4') {
        pdf.line(10, 0, 10, 7);
        pdf.line(0, 10, 7, 10);
        pdf.line(200, 0, 200, 7);
        pdf.line(203, 10, 210, 10);
        pdf.line(10, 291, 10, 300);
        pdf.line(0, 288, 7, 288);
        pdf.line(200, 291, 200, 300);
        pdf.line(203, 288, 210, 288);
        pdf.setLineDashPattern([0.5, 0.5], 0);
        pdf.setDrawColor(105, 105, 105);
        pdf.line(16, 50, 200, 50);
        pdf.line(16, 90, 200, 90);
        pdf.line(16, 100, 200, 100);
        pdf.line(79.4, 116, 136, 116);
        pdf.line(78.2, 100, 79.4, 116);
        pdf.line(137.2, 100, 136, 116);
      }

      if (format === 'a5') {
        pdf.line(7, 0, 7, 5.5);
        pdf.line(0, 7, 5, 7);
        pdf.line(141, 0, 141, 5.5);
        pdf.line(143.5, 7, 148, 7);
        pdf.line(7, 206, 7, 212);
        pdf.line(0, 204, 5, 204);
        pdf.line(141, 206, 141, 212);
        pdf.line(143.5, 204, 148, 204);
        pdf.setLineDashPattern([0.5, 0.5], 0);
        pdf.setDrawColor(105, 105, 105);
        pdf.line(13, 35, 138, 35);
        pdf.line(13, 62, 138, 62);
        pdf.line(13, 70, 138, 70);
        pdf.line(54.8, 84, 99.2, 84);
        pdf.line(54, 70, 54.8, 84);
        pdf.line(100, 70, 99.2, 84);
      }

      if (format === 'a6') {
        pdf.line(5, 0, 5, 3.7);
        pdf.line(0, 5, 3.5, 5);
        pdf.line(100, 0, 100, 3.7);
        pdf.line(101.5, 5, 105, 5);
        pdf.line(5, 145, 5, 149);
        pdf.line(0, 143.5, 3.5, 143.5);
        pdf.line(100, 145, 100, 149);
        pdf.line(101.5, 143.5, 105, 143.5);
        pdf.setLineDashPattern([0.5, 0.5], 0);
        pdf.setDrawColor(105, 105, 105);
        pdf.line(10, 25, 98, 25);
        pdf.line(10, 44, 98, 44);
        pdf.line(10, 48.5, 98, 48.5);
        pdf.line(40, 56, 69.2, 56);
        pdf.line(39.5, 48.5, 40, 56);
        pdf.line(69.7, 48.5, 69.2, 56);
      }

      pdf.save(title.trim() + '_draft_' + format + '.pdf');
    });
  }

  public onSequenceNumberSearch(): void {
    const seqNumberControl: AbstractControl = this.materialForm.get('sequenceNumber');

    if (isNaN(seqNumberControl.value)) {
      this.searchFailed = true;
      return;
    }

    this.materialService.getBySequenceNumberPublished(Number(seqNumberControl.value)).subscribe(
      data => {
        this.searchFailed = true;
        this.recipeTitle = '';
        this.recipeAuthor = '';

        if (data) {
          this.searchFailed = false;
          const foundMaterial: Material = Material.trueCopy(data);
          this.parentId = foundMaterial.getSequenceNumber();
          const author = User.trueCopy(foundMaterial.getUser());
          this.recipeTitle = foundMaterial.getName();
          this.recipeAuthor = `By ${author.getFirstname()} ${author.getLastname()}`;
          this.recipeYear = new Date(foundMaterial.getCreationDate()).getFullYear().toString();
        } else {
          seqNumberControl.reset();
        }
      },
      error => {
        this.searchFailed = true;
        seqNumberControl.reset();
      }
    );
  }
}
