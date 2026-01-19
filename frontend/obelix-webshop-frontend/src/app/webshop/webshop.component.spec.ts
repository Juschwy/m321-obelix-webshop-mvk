import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebshopComponent } from './webshop.component';

describe('WebshopComponent', () => {
  let component: WebshopComponent;
  let fixture: ComponentFixture<WebshopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WebshopComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WebshopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
