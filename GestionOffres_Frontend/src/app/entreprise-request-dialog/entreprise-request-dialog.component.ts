import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from '../services/user.service';
import { authApiURL } from '../config';

@Component({
  selector: 'app-entreprise-request-dialog',
  templateUrl: './entreprise-request-dialog.component.html',
  styleUrls: ['./entreprise-request-dialog.component.css']
})
export class EntrepriseRequestDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<EntrepriseRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userService: UserService
  ) {}

  onClose(): void {
    this.dialogRef.close();
  }

  downloadDocument(type: string, demandeId: string): void {
    const url = `${authApiURL}/download/${type}/${demandeId}`;
    this.userService.downloadDocument(url).subscribe(blob => {
      this.handleDocumentDownload(blob, `${type}_${demandeId}.pdf`);
    }, error => {
      console.error('Download failed:', error);
    });
  }

  private handleDocumentDownload(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    window.URL.revokeObjectURL(url);
    link.remove();
  }
}
