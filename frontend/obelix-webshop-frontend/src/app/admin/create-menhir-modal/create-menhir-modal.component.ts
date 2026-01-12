import { Component, EventEmitter, Output, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AdminService } from '../../services/admin.service';
import { DecorativenessDto, MenhirDto } from '../../models/menhir.dto';

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

  private readonly fb = inject(FormBuilder);
  private readonly adminService = inject(AdminService);

  protected readonly menhirForm: FormGroup;
  protected readonly decorativenessOptions = Object.values(DecorativenessDto);
  protected readonly isSubmitting$$ = signal<boolean>(false);
  protected readonly errorMessage$$ = signal<string | null>(null);
  protected readonly selectedImage$$ = signal<File | null>(null);
  protected readonly imagePreview$$ = signal<string | null>(null);

  constructor() {
    this.menhirForm = this.fb.group({
      weight: ['', [Validators.required, Validators.min(0.1)]],
      stoneType: ['', [Validators.required, Validators.minLength(2)]],
      decorativeness: ['', Validators.required],
      description: [''],
      image: ['']
    });
  }

  protected onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedImage$$.set(input.files[0]);
      
      // Create preview
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview$$.set(e.target.result);
      };
      reader.readAsDataURL(this.selectedImage$$()!);
    }
  }

  protected removeImage(): void {
    this.selectedImage$$.set(null);
    this.imagePreview$$.set(null);
    this.menhirForm.patchValue({ image: '' });
  }

  protected onSubmit(): void {
    if (this.menhirForm.invalid) {
      this.markFormGroupTouched(this.menhirForm);
      return;
    }

    this.isSubmitting$$.set(true);
    this.errorMessage$$.set(null);

    const formValue = this.menhirForm.value;

    // Convert image to base64 if selected
    let imageUrl: string | undefined = undefined;
    if (this.selectedImage$$() && this.imagePreview$$()) {
      imageUrl = this.imagePreview$$() as string; // Already base64 from FileReader
    }

    this.adminService.createMenhir({
      weight: parseFloat(formValue.weight),
      stoneType: formValue.stoneType,
      decorativeness: formValue.decorativeness,
      description: formValue.description || '',
      imageUrl: imageUrl
    }).pipe(first()).subscribe({
      next: (menhir: MenhirDto) => {
        this.isSubmitting$$.set(false);
        this.menhirForm.reset();
        this.selectedImage$$.set(null);
        this.imagePreview$$.set(null);
        this.created.emit(menhir);
        this.closeModal();
      },
      error: (error) => {
        this.isSubmitting$$.set(false);
        this.errorMessage$$.set(error.message || 'An error occurred while creating the menhir.');
      }
    });
  }

  protected closeModal(): void {
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

