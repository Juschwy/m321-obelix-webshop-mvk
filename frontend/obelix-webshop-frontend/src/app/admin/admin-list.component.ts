import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminService } from './admin.service';
import { MenhirDto } from '../models/menhir.dto';
import { MenhirCardComponent } from '../menhir-card/menhir-card.component';
import { CreateMenhirModalComponent } from './create-menhir-modal.component';

@Component({
  selector: 'app-admin-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MenhirCardComponent, CreateMenhirModalComponent],
  templateUrl: './admin-list.component.html',
  styleUrl: './admin-list.component.scss'
})
export class AdminListComponent implements OnInit {
  menhirs: MenhirDto[] = [];
  isLoading = false;
  errorMessage: string | null = null;
  showCreateModal = false;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadMenhirs();
  }

  loadMenhirs(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.adminService.getAllMenhirs().subscribe({
      next: (menhirs) => {
        this.menhirs = menhirs;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.message || 'Failed to load menhirs.';
        this.isLoading = false;
        console.error('Error loading menhirs:', error);
      }
    });
  }

  openCreateModal(): void {
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  onMenhirCreated(menhir: MenhirDto): void {
    // Reload menhirs list
    this.loadMenhirs();
    this.closeCreateModal();
  }
}

