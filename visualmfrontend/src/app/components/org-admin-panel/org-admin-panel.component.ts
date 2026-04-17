import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Material} from '../../models/material';
import {User} from '../../models/user';
import {Report} from '../../models/report';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'app-org-admin-panel',
  templateUrl: './org-admin-panel.component.html',
  styleUrls: ['./org-admin-panel.component.css']
})
export class OrgAdminPanelComponent implements OnInit {
  token: string;
  orgAdmin: any;
  loading = true;
  accessDenied = false;

  // Pending Approval
  pendingMaterials: Material[] = [];
  pendingDataSource: MatTableDataSource<Material>;
  pendingColumns: string[] = ['sequenceNumber', 'name', 'creationDate', 'user', 'action'];

  // Label
  materials: Material[] = [];
  materialDataSource: MatTableDataSource<Material>;
  materialColumns: string[] = ['sequenceNumber', 'name', 'saveStatus', 'creationDate', 'user', 'action'];

  // User
  users: User[] = [];
  userDataSource: MatTableDataSource<User>;
  userColumns: string[] = ['id', 'email', 'firstname'];

  // Reports
  reports: Report[] = [];
  reportDataSource: MatTableDataSource<Report>;
  reportColumns: string[] = ['id', 'message', 'user', 'solved', 'action'];

  // Label Templates
  templates: any[] = [];
  selectedTemplate: any = null;
  editingTemplate: any = null;

  fontFamilies = [
    'Arial, sans-serif',
    'Georgia, serif',
    'Courier New, monospace',
    'Times New Roman, serif',
    'Verdana, sans-serif',
    'Trebuchet MS, sans-serif',
    'Palatino Linotype, serif'
  ];
  layoutVariants = ['classic', 'modern', 'minimal'];
  borderStyles = ['solid', 'dashed', 'dotted', 'double', 'none'];
  logoPositions = ['left', 'right', 'center'];

  @ViewChild('paginatorPending') paginatorPending: MatPaginator;
  @ViewChild('paginatorMaterial') paginatorMaterial: MatPaginator;
  @ViewChild('paginatorUser') paginatorUser: MatPaginator;
  @ViewChild('paginatorReport') paginatorReport: MatPaginator;

  @ViewChild('sortPending') sortPending: MatSort;
  @ViewChild('sortMaterial') sortMaterial: MatSort;
  @ViewChild('sortUser') sortUser: MatSort;
  @ViewChild('sortReport') sortReport: MatSort;

  private baseUrl = `${environment.backend_url}/org-admin/api`;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
    if (!this.token) {
      this.accessDenied = true;
      this.loading = false;
      return;
    }

    this.http.get(`${this.baseUrl}/${this.token}`).subscribe(
      (admin: any) => {
        this.orgAdmin = admin;
        this.loadPendingMaterials();
        this.loadMaterials();
        this.loadUsers();
        this.loadReports();
        this.loadTemplates();
      },
      () => {
        this.accessDenied = true;
        this.loading = false;
      }
    );
  }

  loadPendingMaterials(): void {
    this.http.get<any[]>(`${this.baseUrl}/${this.token}/pending`).subscribe(
      (materials) => {
        this.pendingMaterials = materials.map(m => Object.assign(new Material(), m));
        this.pendingDataSource = new MatTableDataSource<Material>(this.pendingMaterials);
        setTimeout(() => {
          this.pendingDataSource.paginator = this.paginatorPending;
          this.pendingDataSource.sort = this.sortPending;
        });
        this.loading = false;
      },
      () => { this.loading = false; }
    );
  }

  loadMaterials(): void {
    this.http.get<any[]>(`${this.baseUrl}/${this.token}/materials`).subscribe(
      (materials) => {
        this.materials = materials.map(m => Object.assign(new Material(), m));
        this.materialDataSource = new MatTableDataSource<Material>(this.materials);
        setTimeout(() => {
          this.materialDataSource.paginator = this.paginatorMaterial;
          this.materialDataSource.sort = this.sortMaterial;
        });
      }
    );
  }

  loadUsers(): void {
    this.http.get<any[]>(`${this.baseUrl}/${this.token}/users`).subscribe(
      (users) => {
        this.users = users.map(u => Object.assign(new User(), u));
        this.userDataSource = new MatTableDataSource<User>(this.users);
        setTimeout(() => {
          this.userDataSource.paginator = this.paginatorUser;
          this.userDataSource.sort = this.sortUser;
        });
      }
    );
  }

  loadReports(): void {
    this.http.get<any[]>(`${this.baseUrl}/${this.token}/reports`).subscribe(
      (reports) => {
        this.reports = reports.map(r => Object.assign(new Report(), r));
        this.reportDataSource = new MatTableDataSource<Report>(this.reports);
        setTimeout(() => {
          this.reportDataSource.paginator = this.paginatorReport;
          this.reportDataSource.sort = this.sortReport;
        });
      }
    );
  }

  onApprove(material: Material): void {
    this.http.put(`${this.baseUrl}/${this.token}/approve/${material.getSequenceNumber()}`, {}).subscribe(
      () => {
        this.pendingMaterials = this.pendingMaterials.filter(m => m.getSequenceNumber() !== material.getSequenceNumber());
        this.pendingDataSource.data = this.pendingMaterials;
        this.loadMaterials();
        this.snackBar.open('Label approved and published!', 'Close', {duration: 3000});
      },
      () => { this.snackBar.open('Failed to approve label.', 'Close', {duration: 3000}); }
    );
  }

  onReject(material: Material): void {
    this.http.put(`${this.baseUrl}/${this.token}/reject/${material.getSequenceNumber()}`, {}).subscribe(
      () => {
        this.pendingMaterials = this.pendingMaterials.filter(m => m.getSequenceNumber() !== material.getSequenceNumber());
        this.pendingDataSource.data = this.pendingMaterials;
        this.loadMaterials();
        this.snackBar.open('Label rejected and returned to draft.', 'Close', {duration: 3000});
      },
      () => { this.snackBar.open('Failed to reject label.', 'Close', {duration: 3000}); }
    );
  }

  onReportToggle(report: any): void {
    const updated = {solved: !report.solved};
    this.http.put(`${this.baseUrl}/${this.token}/reports/${report.id}`, updated).subscribe(
      () => {
        report.solved = !report.solved;
        this.snackBar.open(report.solved ? 'Marked as solved' : 'Marked as not solved', 'Close', {duration: 3000});
      },
      () => { this.snackBar.open('Failed to update report.', 'Close', {duration: 3000}); }
    );
  }

  onSettingsSave(): void {
    const updates = {
      organisation: this.orgAdmin.organisation,
      logoPath: this.orgAdmin.logoPath
    };
    this.http.put(`${this.baseUrl}/${this.token}`, updates).subscribe(
      () => { this.snackBar.open('Settings saved!', 'Close', {duration: 3000}); },
      () => { this.snackBar.open('Failed to save settings.', 'Close', {duration: 3000}); }
    );
  }

  onLogoFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) { return; }
    const reader = new FileReader();
    reader.onload = () => {
      this.orgAdmin.logoPath = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  applyFilterMaterial(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (this.materialDataSource) { this.materialDataSource.filter = filterValue.trim().toLowerCase(); }
  }

  applyFilterUser(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (this.userDataSource) { this.userDataSource.filter = filterValue.trim().toLowerCase(); }
  }

  applyFilterReport(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (this.reportDataSource) { this.reportDataSource.filter = filterValue.trim().toLowerCase(); }
  }

  // === Label Template Methods ===

  loadTemplates(): void {
    this.http.get<any[]>(`${this.baseUrl}/${this.token}/templates`).subscribe(
      (templates) => {
        this.templates = templates;
        if (this.templates.length > 0) {
          this.selectedTemplate = this.templates[0];
          this.editingTemplate = {...this.templates[0]};
        }
      }
    );
  }

  selectTemplate(template: any): void {
    this.selectedTemplate = template;
    this.editingTemplate = {...template};
  }

  onTemplateSave(): void {
    if (!this.editingTemplate) { return; }

    if (this.editingTemplate.id) {
      this.http.put(`${this.baseUrl}/${this.token}/templates/${this.editingTemplate.id}`, this.editingTemplate).subscribe(
        (saved: any) => {
          const idx = this.templates.findIndex(t => t.id === saved.id);
          if (idx >= 0) { this.templates[idx] = saved; }
          this.selectedTemplate = saved;
          this.editingTemplate = {...saved};
          this.snackBar.open('Template saved!', 'Close', {duration: 3000});
        },
        () => { this.snackBar.open('Failed to save template.', 'Close', {duration: 3000}); }
      );
    } else {
      this.http.post(`${this.baseUrl}/${this.token}/templates`, this.editingTemplate).subscribe(
        (saved: any) => {
          this.templates.push(saved);
          this.selectedTemplate = saved;
          this.editingTemplate = {...saved};
          this.snackBar.open('Template created!', 'Close', {duration: 3000});
        },
        () => { this.snackBar.open('Failed to create template.', 'Close', {duration: 3000}); }
      );
    }
  }

  onNewTemplate(): void {
    this.editingTemplate = {
      name: 'New Template',
      primaryColor: '#000000',
      secondaryColor: '#333333',
      backgroundColor: '#ffffff',
      fontFamily: 'Calibri Light, Roboto Light, sans-serif',
      titleFontSize: '27pt',
      bodyFontSize: '17pt',
      borderStyle: 'none',
      borderColor: '#000000',
      logoPosition: 'right',
      showQrCode: true,
      headerText: '',
      layoutVariant: 'classic'
    };
    this.selectedTemplate = null;
  }

  onDeleteTemplate(template: any): void {
    if (!template.id) { return; }
    this.http.delete(`${this.baseUrl}/${this.token}/templates/${template.id}`).subscribe(
      () => {
        this.templates = this.templates.filter(t => t.id !== template.id);
        if (this.templates.length > 0) {
          this.selectTemplate(this.templates[0]);
        } else {
          this.selectedTemplate = null;
          this.editingTemplate = null;
        }
        this.snackBar.open('Template deleted.', 'Close', {duration: 3000});
      },
      () => { this.snackBar.open('Failed to delete template.', 'Close', {duration: 3000}); }
    );
  }
}
