import {Component, OnDestroy, OnInit} from '@angular/core';
import {Material} from 'src/app/models/material';
import {MaterialTag} from 'src/app/models/materialtag.enum';
import {MaterialType} from 'src/app/models/material-type.enum';
import {SaveStatus} from 'src/app/models/save-status.enum';
import {MaterialsService} from 'src/app/services/materials.service';
import {Tag} from '../../models/tag';
import {Ingredient} from '../../models/ingredient';
import {MaterialIngredient} from '../../models/material-ingredient';
import {User} from '../../models/user';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';

// One searchable group of a label, e.g. its title or its ingredients, and how much a hit in it counts
interface SearchField {
  enabled: boolean;
  weight: number;
  values: string[];
}

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, OnDestroy {
  public allMaterials: Material[] = [];
  public matchingMaterials: Material[] = [];
  public searchQuery: string = '';
  public tagsSelected: boolean = true;
  public numberSelected: boolean = true;
  public titleSelected: boolean = true;
  public ingredientSelected: boolean = true;
  public userSelected: boolean = true;
  public typeSelected: boolean = true;
  public otherSelected: boolean = true;
  public materialTag = MaterialTag;
  loadingDone: boolean = false;
  searchQuerySubscription: Subscription;

  constructor(private materialService: MaterialsService, private activatedRoute: ActivatedRoute, private router: Router) {
  }

  public getImageSource(imageValue: string): string {
    if (!imageValue) {
      return '';
    }

    if (imageValue.startsWith('data:') || imageValue.startsWith('/')) {
      return imageValue;
    }

    return `data:image/png;base64,${imageValue}`;
  }

  ngOnInit(): void {
    this.materialService.getAll().subscribe(materials => {
      // Only display PUBLISHED labels
      this.allMaterials = (materials || [])
        .map(material => Object.assign(new Material(), material))
        .filter(material => material.getSaveStatus() === SaveStatus.PUBLISHED);
      this.matchingMaterials = this.allMaterials;
      this.loadingDone = true;

      this.searchQuerySubscription = this.activatedRoute.queryParams.subscribe(value => {
        this.searchQuery = value.search || '';
        this.runSearch();
      });
    });
  }

  ngOnDestroy(): void {
    this.searchQuerySubscription?.unsubscribe();
  }

  // Display name of whoever made the label; anonymous labels have no user but may name an author in the reference
  public makerName(material: Material): string {
    if (material.getUser()) {
      const user: User = Object.assign(new User(), material.getUser());
      const name = `${user.getFirstname() || ''} ${user.getLastname() || ''}`.trim();
      if (name) {
        return name;
      }
    }
    return this.referenceAuthor(material) || 'Anonymous';
  }

  private referenceAuthor(material: Material): string {
    const reference = material.getReference();
    if (reference && reference.startsWith('By ')) {
      return reference.substring(3).split(' - ')[0].trim();
    }
    return '';
  }

  // Re-run the current search, e.g. after a field checkbox changed
  public runSearch(): void {
    const tokens = this.normalize(this.searchQuery).split(' ').filter(token => token.length > 0);

    if (tokens.length === 0) {
      this.matchingMaterials = this.allMaterials;
      return;
    }

    const scored: { material: Material, score: number }[] = [];
    for (const material of this.allMaterials) {
      const fields = this.searchFields(material);
      let score = 0;
      let allTokensFound = true;

      // Every word must be found somewhere; its score is the best-weighted field it occurs in
      for (const token of tokens) {
        const tokenScore = this.scoreToken(token, fields);
        if (tokenScore === 0) {
          allTokensFound = false;
          break;
        }
        score += tokenScore;
      }

      if (allTokensFound) {
        scored.push({material, score});
      }
    }

    this.matchingMaterials = scored
      .sort((a, b) => b.score - a.score)
      .map(entry => entry.material);
  }

  private scoreToken(token: string, fields: SearchField[]): number {
    let best = 0;
    for (const field of fields) {
      if (!field.enabled) {
        continue;
      }
      for (const value of field.values) {
        if (value === token) {
          best = Math.max(best, field.weight * 2);
        } else if (value.includes(token)) {
          best = Math.max(best, field.weight);
        }
      }
    }
    return best;
  }

  private searchFields(material: Material): SearchField[] {
    const user: User = material.getUser() ? Object.assign(new User(), material.getUser()) : null;
    const tags: string[] = (material.getTags() || []).map(tagValue => {
      const tag: Tag = Object.assign(new Tag(), tagValue);
      return [tag.getName(), MaterialTag[tag.getName()]].join(' ');
    });
    const ingredients: string[] = (material.getMaterialIngredients() || []).map(value => {
      const materialIngredient: MaterialIngredient = Object.assign(new MaterialIngredient(), value);
      const ingredient: Ingredient = Object.assign(new Ingredient(), materialIngredient.getIngredient());
      return ingredient.getName();
    });
    const published = material.getSequenceNumberPublished();
    const creationYear = material.getCreationDate() ? new Date(material.getCreationDate()).getFullYear().toString() : '';

    const field = (enabled: boolean, weight: number, values: string[]): SearchField => ({
      enabled,
      weight,
      // Tokens are matched word by word, so multi-word values are also split into their words
      values: values
        .filter(value => value != null && `${value}`.length > 0)
        .map(value => this.normalize(`${value}`))
        .reduce((all, value) => all.concat(value, value.split(' ')), [] as string[])
        .filter(value => value.length > 0)
    });

    return [
      field(this.titleSelected, 25, [material.getName()]),
      field(this.numberSelected, 10, [
        published != null ? published.toString() : null,
        material.getFormattedSequenceNumber(),
        material.getFormattedSequenceNumber()?.replace(/^0+/, '')
      ]),
      field(this.userSelected, 8, [
        user?.getFirstname(), user?.getLastname(), user?.getEmail(),
        this.referenceAuthor(material), material.referenceEmail,
        user ? null : 'anonymous'
      ]),
      field(this.typeSelected, 6, [material.getType(), MaterialType[material.getType()]]),
      field(this.tagsSelected, 5, [
        ...tags,
        ...(material.getPostProcessingTags() || '').split('|')
      ]),
      field(this.ingredientSelected, 8, ingredients),
      field(this.otherSelected, 2, [
        material.getChanges(),
        material.getSteps(),
        material.getReference(),
        material.getOrganisation(),
        creationYear
      ])
    ];
  }

  // Lower case, "#0012" -> "0012", and separators like "-", "_", "|" and "/" treated as spaces
  private normalize(text: string): string {
    return (text || '')
      .toLowerCase()
      .replace(/#/g, '')
      .replace(/[_|/,;:()\-]+/g, ' ')
      .replace(/\s+/g, ' ')
      .trim();
  }

  urlParameters(searchQuery: string) {
    this.router.navigate(
      [],
      {
        relativeTo: this.activatedRoute,
        queryParams: {search: searchQuery || null},
        queryParamsHandling: 'merge'
      });
  }

  OnInput(event: any) {
    this.urlParameters(event.target.value);
  }

  exportLabels(): void {
    let date = (new Date()).toLocaleDateString('nl-NL');

    const csvArray: Array<string> = [];

    //Add header to CSV
    csvArray.push('sep=\t');
    csvArray.push('\n');
    const header = Object.keys(this.matchingMaterials[0]);
    header.forEach(x => {
      if (!(x.toLowerCase().includes('overviewurl')) && !(x.toLowerCase().includes('closeupurl'))) {
        csvArray.push(x + '\t ');
      }
    });
    csvArray.push('\n');

    //Add materials to CSV
    this.matchingMaterials.forEach(x => {
        csvArray.push(
          x.getFormattedSequenceNumber() + '\t ' +
          x.getName() + '\t ' +
          x.getChanges() + '\t ' +
          x.getCreationDate() + '\t ' +
          x.getSteps() + '\t ' +
          x.getQRCodeURL() + '\t ');

        //Assign object type for the tags
        for (let i = 0; i < x.getTags().length; i++) {
          const currentTag: Tag = Object.assign(new Tag(), x.getTags()[i]);
          csvArray.push(currentTag.getName() + ' (' + currentTag.getId() + ') | ');
        }
        csvArray.push('\t ');

        //Assign object type for the ingredients
        for (let i = 0; i < x.getMaterialIngredients().length; i++) {
          const currentMaterialIngredient: MaterialIngredient = Object.assign(new MaterialIngredient(), x.getMaterialIngredients()[i]);
          const currentIngredient: Ingredient = Object.assign(new Ingredient(), currentMaterialIngredient.getIngredient());
          csvArray.push(currentIngredient.getName() + ' ' + currentIngredient.getType() + ' (' + currentIngredient.getId() + ') | ');
        }
        csvArray.push('\t ');

        csvArray.push(
          x.getSaveStatus() + '\t ' +
          x.getType() + '\t '
        );

        //Assign object type for the user
        const currentUser: User = Object.assign(new User(), x.getUser());
        csvArray.push(currentUser.getOrganisation() + ' ' + currentUser.getFirstname() + ' ' + currentUser.getLastname() + ' (' + currentUser.getId() + ')');
        csvArray.push('\t ');

        if (x.getParentId() == null) {
          csvArray.push('No parent' + '\t ');
        } else {
          csvArray.push(
            x.getParentId() + '\t '
          );
        }

        csvArray.push(
          x.getReference() + '\t ' + '\n'
        );
      }
    );

    const myblob = new Blob(csvArray, {
      type: 'text/csv'
    });

    const a = document.createElement('a');
    const url = window.URL.createObjectURL(myblob);

    a.href = url;
    a.download = 'LabelExport - ' + date + '.csv';
    a.click();
    window.URL.revokeObjectURL(url);
    a.remove();
  }


}
