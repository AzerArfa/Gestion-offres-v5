// password-change-warning.component.ts
import { Component, OnInit, Input } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-password-change-warning',
  templateUrl: './password-change-warning.component.html',
  styleUrls: ['./password-change-warning.component.css']
})
export class PasswordChangeWarningComponent implements OnInit {
  @Input() userId!: string;
  remainingDays: number | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.checkPasswordChangeNecessity(this.userId).subscribe(
      (remainingDays: number) => {
        if (remainingDays >= 0) {
          this.remainingDays = remainingDays;
        }
      },
      (error) => {
        console.error('Error checking password change necessity', error);
      }
    );
  }
}
