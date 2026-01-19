import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { MenhirDto, CreateMenhirRequest } from '../models/menhir.dto';
import { BASE_PATH_DEFAULT } from '../../api/tokens';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private readonly http = inject(HttpClient);
  private readonly basePath = inject(BASE_PATH_DEFAULT); // e.g., http://localhost:8080

  /**
   * Fetch all menhirs from backend.
   * GET {basePath}/api/menhirs
   */
  public getAllMenhirs(): Observable<MenhirDto[]> {
    const url = `${this.basePath}/api/menhirs`;
    return this.http.get<MenhirDto[]>(url).pipe(
      map((items) => (items || []).map((m) => ({
        id: m.id,
        weight: m.weight,
        stoneType: m.stoneType,
        decorativeness: m.decorativeness,
        description: m.description,
        imageUrl: (m as any).imageUrl // backend may not provide; keep if present
      }) as MenhirDto))
    );
  }

  /**
   * Create a menhir in backend.
   * POST {basePath}/api/menhir
   * Backend returns void; caller should reload list after success.
   */
  public createMenhir(menhir: CreateMenhirRequest): Observable<void> {
    const url = `${this.basePath}/api/menhir`;
    const body = {
      // Backend MenhirDto fields; id omitted for create
      weight: menhir.weight,
      stoneType: menhir.stoneType,
      decorativeness: menhir.decorativeness,
      description: menhir.description
    };
    return this.http.post<void>(url, body);
  }
}
