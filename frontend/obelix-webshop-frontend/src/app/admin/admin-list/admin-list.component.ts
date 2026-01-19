import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { first } from 'rxjs/operators';
import { AdminService } from '../../services/admin.service';
import { MenhirDto } from '../../models/menhir.dto';
import { MenhirCardComponent } from '../../menhir-card/menhir-card.component';
import { CreateMenhirModalComponent } from '../create-menhir-modal/create-menhir-modal.component';

@Component({
  selector: 'app-admin-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MenhirCardComponent, CreateMenhirModalComponent],
  templateUrl: './admin-list.component.html',
  styleUrl: './admin-list.component.scss'
})
export class AdminListComponent implements OnInit {
  private readonly adminService = inject(AdminService);
  
  protected readonly menhirs$$ = signal<MenhirDto[]>([]);
  protected readonly isLoading$$ = signal<boolean>(false);
  protected readonly errorMessage$$ = signal<string | null>(null);
  protected readonly showCreateModal$$ = signal<boolean>(false);

  public ngOnInit(): void {
    this.loadMenhirs();
  }

  protected loadMenhirs(): void {
    this.isLoading$$.set(true);
    this.errorMessage$$.set(null);

    this.adminService.getAllMenhirs()
      .pipe(first())
      .subscribe({
        next: (menhirs) => {
          this.menhirs$$.set(menhirs);
          this.isLoading$$.set(false);
        },
        error: (error) => {
          this.errorMessage$$.set(error.message || 'Failed to load menhirs.');
          this.isLoading$$.set(false);
        }
      });
  }

  protected openCreateModal(): void {
    this.showCreateModal$$.set(true);
  }

  protected closeCreateModal(): void {
    this.showCreateModal$$.set(false);
  }

  protected onMenhirCreated(): void {
    this.loadMenhirs();
    this.closeCreateModal();
  }
}
