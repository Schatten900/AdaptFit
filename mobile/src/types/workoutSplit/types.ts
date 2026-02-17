import { WorkoutDayResponse } from '~/types';

export interface SplitWorkoutDayRequest {
  workoutDayId: number;
  dayOfWeek: number;
  dayOrder: number;
}

export interface SplitWorkoutDayResponse {
  id: number;
  splitId: number;
  workoutDayId: number;
  workoutDayName: string;
  dayOfWeek: number;
  dayOrder: number;
}

export interface CreateSplitRequest {
  name: string;
  description?: string;
  splitWorkoutDays: SplitWorkoutDayRequest[];
}

export interface UpdateSplitRequest {
  name?: string;
  description?: string;
  active?: boolean;
  splitWorkoutDays?: SplitWorkoutDayRequest[];
}

export interface SplitResponse {
  id: number;
  name: string;
  description?: string;
  active: boolean;
  splitWorkoutDays: SplitWorkoutDayResponse[];
  createdAt?: string;
  updatedAt?: string;
}
