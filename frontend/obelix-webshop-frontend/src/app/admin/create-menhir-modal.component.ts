import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from './admin.service';
import { DecorativenessDto, MenhirDto } from '../models/menhir.dto';

@Component({
  selector: 'app-create-menhir-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-menhir-modal.component.html',
  styleUrl: './create-menhir-modal.component.scss'
})
export class CreateMenhirModalComponent {
  @Output() close = new EventEmitter<void>();
  @Output() created = new EventEmitter<MenhirDto>();

  menhirForm: FormGroup;
  decorativenessOptions = Object.values(DecorativenessDto);
  isSubmitting = false;
  errorMessage: string | null = null;
  selectedImage: File | null = null;
  imagePreview: string | null = null;

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService
  ) {
    this.menhirForm = this.fb.group({
      weight: ['', [Validators.required, Validators.min(0.1)]],
      stoneType: ['', [Validators.required, Validators.minLength(2)]],
      decorativeness: ['', Validators.required],
      description: [''],
      image: ['']
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedImage = input.files[0];
      
      // Create preview
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(this.selectedImage);
    }
  }

  removeImage(): void {
    this.selectedImage = null;
    this.imagePreview = null;
    this.menhirForm.patchValue({ image: '' });
  }

  onSubmit(): void {
    if (this.menhirForm.invalid) {
      this.markFormGroupTouched(this.menhirForm);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = null;

    const formValue = this.menhirForm.value;

    // Convert image to base64 if selected
    let imageUrl: string | undefined = undefined;
    if (this.selectedImage && this.imagePreview) {
      imageUrl = this.imagePreview as string; // Already base64 from FileReader
    }

    this.adminService.createMenhir({
      weight: parseFloat(formValue.weight),
      stoneType: formValue.stoneType,
      decorativeness: formValue.decorativeness,
      description: formValue.description || '',
      imageUrl: imageUrl
    }).subscribe({
      next: (menhir: MenhirDto) => {
        this.isSubmitting = false;
        this.menhirForm.reset();
        this.selectedImage = null;
        this.imagePreview = null;
        this.created.emit(menhir);
        this.closeModal();
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.message || 'An error occurred while creating the menhir.';
        console.error('Error creating menhir:', error);
      }
    });
  }

  closeModal(): void {
    this.close.emit();
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
      description: 'Description',
      image: 'Image'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.menhirForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }
}

