import {Component, OnInit} from '@angular/core';
import {Material} from 'src/app/models/material';
import {MaterialTag} from 'src/app/models/materialtag.enum';
import {MaterialsService} from 'src/app/services/materials.service';

export type HomeSortOption = 'newest' | 'oldest' | 'az' | 'za';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public materials: Material[] = [];
  public materialTag = MaterialTag;
  public sortOption: HomeSortOption = 'newest';
  loadingDone: boolean = false;

  constructor(private materialService: MaterialsService) {
  }

  ngOnInit(): void {
    this.materialService.getAllPublished().subscribe(materials => {
      this.materials = materials.filter(material => material).map(material => Material.trueCopy(material));
      this.sortMaterials();
      this.loadingDone = true;
    });
  }

  // Recipes with an image always come first; the chosen sort applies within each group.
  public sortMaterials(): void {
    this.materials.sort((a, b) => {
      const imageDiff = Number(this.hasImage(b)) - Number(this.hasImage(a));
      if (imageDiff !== 0) {
        return imageDiff;
      }

      switch (this.sortOption) {
        case 'az':
          return this.compareNames(a, b);
        case 'za':
          return this.compareNames(b, a);
        case 'oldest':
          return this.compareDates(a, b);
        default:
          return this.compareDates(b, a);
      }
    });
  }

  private hasImage(material: Material): boolean {
    return !!material.getOverviewURL();
  }

  private compareNames(a: Material, b: Material): number {
    return (a.getName() || '').localeCompare(b.getName() || '', undefined, {sensitivity: 'base', numeric: true});
  }

  private compareDates(a: Material, b: Material): number {
    const dateDiff = this.getTime(a) - this.getTime(b);
    // Many labels share a creation day, so fall back to insertion order
    return dateDiff !== 0 ? dateDiff : (a.getSequenceNumber() || 0) - (b.getSequenceNumber() || 0);
  }

  private getTime(material: Material): number {
    const date = material.getCreationDate();
    const time = date ? new Date(date).getTime() : NaN;
    return isNaN(time) ? 0 : time;
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
}
