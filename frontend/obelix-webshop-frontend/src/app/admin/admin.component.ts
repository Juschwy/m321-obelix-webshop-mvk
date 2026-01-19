import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { AdminService } from '../services/admin.service';
import { DecorativenessDto, MenhirDto } from '../models/menhir.dto';

@Component({
  selector: 'app-admin-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminCreateComponent {
  private readonly fb = inject(FormBuilder);
  private readonly adminService = inject(AdminService);
  private readonly router = inject(Router);

  protected readonly menhirForm: FormGroup;
  protected readonly decorativenessOptions = Object.values(DecorativenessDto);
  protected readonly isSubmitting$$ = signal<boolean>(false);
  protected readonly successMessage$$ = signal<string | null>(null);
  protected readonly errorMessage$$ = signal<string | null>(null);
  protected readonly createdMenhir$$ = signal<MenhirDto | null>(null);

  constructor() {
    this.menhirForm = this.fb.group({
      weight: ['', [Validators.required, Validators.min(0.1)]],
      stoneType: ['', [Validators.required, Validators.minLength(2)]],
      decorativeness: ['', Validators.required],
      description: ['']
    });
  }

  protected onSubmit(): void {
    if (this.menhirForm.invalid) {
      this.markFormGroupTouched(this.menhirForm);
      return;
    }

    this.isSubmitting$$.set(true);
    this.errorMessage$$.set(null);
    this.successMessage$$.set(null);
    this.createdMenhir$$.set(null);

    const formValue = this.menhirForm.value;

    this.adminService.createMenhir({
      weight: parseFloat(formValue.weight),
      stoneType: formValue.stoneType,
      decorativeness: formValue.decorativeness,
      description: formValue.description || ''
    }).pipe(first()).subscribe({
      next: (menhir: MenhirDto) => {
        this.isSubmitting$$.set(false);
        this.successMessage$$.set('Menhir successfully created!');
        this.createdMenhir$$.set(menhir);
        this.menhirForm.reset();
        // Navigate back to admin list after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/admin']);
        }, 2000);
      },
      error: (error) => {
        this.isSubmitting$$.set(false);
        this.errorMessage$$.set(error.message || 'An error occurred while creating the menhir.');
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

  protected getErrorMessage(fieldName: string): string {
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

  protected isFieldInvalid(fieldName: string): boolean {
    const control = this.menhirForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }
}

