import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, delay } from 'rxjs';
import { MenhirDto, CreateMenhirRequest } from '../models/menhir.dto';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  // TODO: Replace with actual backend URL from environment configuration
  private readonly apiUrl = 'http://localhost:8080/api/admin/menhirs';

  constructor(private http: HttpClient) {}

  /**
   * Gets all Menhirs.
   * Currently returns a mock response. 
   * To integrate with backend, uncomment the HTTP call and remove the mock implementation.
   * 
   * Expected backend endpoint: GET /api/menhirs
   * Expected response: MenhirDto[]
   */
  getAllMenhirs(): Observable<MenhirDto[]> {
    // MOCK IMPLEMENTATION - Remove when backend is ready
    const defaultMenhirs: MenhirDto[] = [
      {
        id: '1',
        weight: 2.8,
        stoneType: 'Granite',
        decorativeness: 'PLAIN' as any,
        description: 'A timeless vertical stone monument.'
      },
      {
        id: '2',
        weight: 3.5,
        stoneType: 'Marble',
        decorativeness: 'DECORATED' as any,
        description: 'An elegantly decorated menhir with intricate carvings.'
      },
      {
        id: '3',
        weight: 4.2,
        stoneType: 'Sandstone',
        decorativeness: 'MASTERWORK' as any,
        description: 'A masterfully crafted menhir, showcasing the finest stonework.'
      }
    ];

    // Load stored menhirs from localStorage and merge with defaults
    let storedMenhirs: MenhirDto[] = [];
    try {
      const stored = localStorage.getItem('menhirs');
      if (stored) {
        storedMenhirs = JSON.parse(stored);
      }
    } catch (e) {
      console.error('Error loading menhirs from localStorage', e);
    }

    // Combine defaults with stored (stored take precedence by id, then append new ones)
    const existingIds = new Set(defaultMenhirs.map(m => m.id));
    const newStoredMenhirs = storedMenhirs.filter(m => !existingIds.has(m.id));
    const allMenhirs = [...defaultMenhirs, ...newStoredMenhirs];
    
    // Simulate network delay
    return of(allMenhirs).pipe(delay(300));

    // REAL IMPLEMENTATION (uncomment when backend is ready):
    // return this.http.get<MenhirDto[]>('http://localhost:8080/api/menhirs');
  }

  /**
   * Creates a new Menhir.
   * Currently returns a mock response. 
   * To integrate with backend, uncomment the HTTP call and remove the mock implementation.
   * 
   * Expected backend endpoint: POST /api/admin/menhirs
   * Expected request body: CreateMenhirRequest
   * Expected response: MenhirDto
   */
  createMenhir(menhir: CreateMenhirRequest): Observable<MenhirDto> {
    // MOCK IMPLEMENTATION - Remove when backend is ready
    const mockResponse: MenhirDto = {
      id: crypto.randomUUID(),
      weight: menhir.weight,
      stoneType: menhir.stoneType,
      decorativeness: menhir.decorativeness,
      description: menhir.description,
      imageUrl: menhir.imageUrl
    };
    
    // Store in localStorage for persistence (mock)
    this.storeMenhirInLocalStorage(mockResponse);
    
    // Simulate network delay
    return of(mockResponse).pipe(delay(500));

    // REAL IMPLEMENTATION (uncomment when backend is ready):
    // const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    // return this.http.post<MenhirDto>(this.apiUrl, menhir, { headers });
  }

  private storeMenhirInLocalStorage(menhir: MenhirDto): void {
    try {
      const stored = localStorage.getItem('menhirs');
      const menhirs: MenhirDto[] = stored ? JSON.parse(stored) : [];
      menhirs.push(menhir);
      localStorage.setItem('menhirs', JSON.stringify(menhirs));
    } catch (e) {
      console.error('Error storing menhir in localStorage', e);
    }
  }
}

