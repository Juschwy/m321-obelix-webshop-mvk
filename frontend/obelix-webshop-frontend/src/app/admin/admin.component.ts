import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AdminService } from './admin.service';
import { DecorativenessDto, MenhirDto } from '../models/menhir.dto';

@Component({
  selector: 'app-admin-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminCreateComponent {
  menhirForm: FormGroup;
  decorativenessOptions = Object.values(DecorativenessDto);
  isSubmitting = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  createdMenhir: MenhirDto | null = null;

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private router: Router
  ) {
    this.menhirForm = this.fb.group({
      weight: ['', [Validators.required, Validators.min(0.1)]],
      stoneType: ['', [Validators.required, Validators.minLength(2)]],
      decorativeness: ['', Validators.required],
      description: ['']
    });
  }

  onSubmit(): void {
    if (this.menhirForm.invalid) {
      this.markFormGroupTouched(this.menhirForm);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;
    this.successMessage = null;
    this.createdMenhir = null;

    const formValue = this.menhirForm.value;

    this.adminService.createMenhir({
      weight: parseFloat(formValue.weight),
      stoneType: formValue.stoneType,
      decorativeness: formValue.decorativeness,
      description: formValue.description || ''
    }).subscribe({
      next: (menhir: MenhirDto) => {
        this.isSubmitting = false;
        this.successMessage = 'Menhir successfully created!';
        this.createdMenhir = menhir;
        this.menhirForm.reset();
        // Navigate back to admin list after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/admin']);
        }, 2000);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.message || 'An error occurred while creating the menhir.';
        console.error('Error creating menhir:', error);
      }
    });
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  getErrorMessage(fieldName: string): string {
    const control = this.menhirForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${this.getFieldLabel(fieldName)} is required`;
    }
    if (control?.hasError('min')) {
      return `${this.getFieldLabel(fieldName)} must be at least ${control.errors?.['min'].min}`;
    }
    if (control?.hasError('minlength')) {
      return `${this.getFieldLabel(fieldName)} must be at least ${control.errors?.['minlength'].requiredLength} characters`;
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      weight: 'Weight',
      stoneType: 'Stone Type',
      decorativeness: 'Decorativeness',
      description: 'Description'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.menhirForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }
}

