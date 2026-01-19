import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { AdminCreateComponent } from './admin.component';
import { AdminService } from '../services/admin.service';
import { of, throwError } from 'rxjs';
import { DecorativenessDto } from '../models/menhir.dto';

describe('AdminCreateComponent', () => {
  let component: AdminCreateComponent;
  let fixture: ComponentFixture<AdminCreateComponent>;
  let adminService: jasmine.SpyObj<AdminService>;

  beforeEach(async () => {
    const adminServiceSpy = jasmine.createSpyObj('AdminService', ['createMenhir']);

    await TestBed.configureTestingModule({
      imports: [AdminCreateComponent, ReactiveFormsModule],
      providers: [
        { provide: AdminService, useValue: adminServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminCreateComponent);
    component = fixture.componentInstance;
    adminService = TestBed.inject(AdminService) as jasmine.SpyObj<AdminService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.menhirForm.get('weight')?.value).toBe('');
    expect(component.menhirForm.get('stoneType')?.value).toBe('');
    expect(component.menhirForm.get('decorativeness')?.value).toBe('');
    expect(component.menhirForm.get('description')?.value).toBe('');
  });

  it('should validate required fields', () => {
    const form = component.menhirForm;
    expect(form.valid).toBeFalsy();

    form.patchValue({
      weight: 2.5,
      stoneType: 'Granite',
      decorativeness: DecorativenessDto.PLAIN
    });

    expect(form.valid).toBeTruthy();
  });

  it('should call adminService.createMenhir on submit', () => {
    adminService.createMenhir.and.returnValue(of(void 0));

    component.menhirForm.patchValue({
      weight: 2.5,
      stoneType: 'Granite',
      decorativeness: DecorativenessDto.PLAIN,
      description: 'Test menhir'
    });

    component.onSubmit();

    expect(adminService.createMenhir).toHaveBeenCalled();
    expect(component.successMessage$$()).toBe('Menhir successfully created!');
  });

  it('should handle error on submit', () => {
    adminService.createMenhir.and.returnValue(
      throwError(() => ({ message: 'Test error' }))
    );

    component.menhirForm.patchValue({
      weight: 2.5,
      stoneType: 'Granite',
      decorativeness: DecorativenessDto.PLAIN
    });

    component.onSubmit();

    expect(component.errorMessage$$()).toBe('Test error');
    expect(component.isSubmitting$$()).toBeFalsy();
  });
});

