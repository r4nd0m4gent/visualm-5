import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  // 20MB in bytes
  private static readonly MAX_SIZE: number = 2000000;
  private static readonly WHITELIST: [string, string, string] = ['jpg', 'png', 'jpeg'];
  private static instances = 0;
  public mediaDataURL: string;
  private valid: boolean;
  private errorMessage: string;
  private fileName: string;
  public componentId;

  constructor() {
    this.valid = true;
    this.fileName = '';
    this.componentId = ++FileUploadComponent.instances;
  }

  ngOnInit(): void {
  }

  public handleFileUpload(files: FileList): void {
    const file: File = files.item(0);

    if (this.validate(file)) {
      this.fileName = file.name;

      const reader: FileReader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        const dataURL = reader.result.toString();
        this.compressImage(dataURL, file.type).then(compressed => {
          this.mediaDataURL = compressed;
        });
      };
    }
  }

  private compressImage(dataURL: string, mimeType: string): Promise<string> {
    return new Promise(resolve => {
      const img = new Image();
      img.onload = () => {
        const MAX_DIMENSION = 1200;
        const QUALITY = 0.75;
        let { width, height } = img;

        if (width > MAX_DIMENSION || height > MAX_DIMENSION) {
          const scale = Math.min(MAX_DIMENSION / width, MAX_DIMENSION / height);
          width = Math.round(width * scale);
          height = Math.round(height * scale);
        }

        const canvas = document.createElement('canvas');
        canvas.width = width;
        canvas.height = height;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(img, 0, 0, width, height);

        const outputType = mimeType === 'image/png' ? 'image/png' : 'image/jpeg';
        resolve(canvas.toDataURL(outputType, QUALITY));
      };
      img.onerror = () => resolve(dataURL);
      img.src = dataURL;
    });
  }

  private validate(file: File): boolean {
    const fileType: string = file.type.split('/')[1];

    this.valid = true;
    this.errorMessage = '';

    if (file.size > FileUploadComponent.MAX_SIZE) {
      this.valid = false;
      this.errorMessage = 'File size cannot exceed 2MB.';
    }

    if (!FileUploadComponent.WHITELIST.includes(fileType)) {
      this.valid = false;
      this.errorMessage = `File cannot be of type: ${fileType}`;
    }

    return this.valid;
  }

  public isValid(): boolean {
    return this.valid;
  }

  public getErrorMessage(): string {
    return this.errorMessage;
  }

  public getFileName(): string {
    return this.fileName;
  }
}
