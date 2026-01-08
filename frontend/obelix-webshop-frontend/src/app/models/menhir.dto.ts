export interface MenhirDto {
  id: string;
  weight: number;
  stoneType: string;
  decorativeness: DecorativenessDto;
  description: string;
  imageUrl?: string;
}

export enum DecorativenessDto {
  PLAIN = 'PLAIN',
  SIMPLE = 'SIMPLE',
  DECORATED = 'DECORATED',
  ORNATE = 'ORNATE',
  MASTERWORK = 'MASTERWORK'
}

export interface CreateMenhirRequest {
  weight: number;
  stoneType: string;
  decorativeness: DecorativenessDto;
  description: string;
  imageUrl?: string;
}

