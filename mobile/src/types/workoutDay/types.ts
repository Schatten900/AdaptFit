import { WorkoutExerciseResponse, WorkoutExerciseRequest } from '~/types'

export interface CreateWorkoutDayRequest {
  name: string;
  description?: string;
  exercises: WorkoutExerciseRequest[];
}

export interface UpdateWorkoutDayRequest {
  name?: string;
  description?: string;
  exercises?: WorkoutExerciseRequest[];
}

export interface WorkoutDayResponse {
  id: number;
  name: string;
  description?: string;
  dayOfWeek?: number;
  dayOrder?: number;
  exercises: WorkoutExerciseResponse[];
  createdAt?: string;
  updatedAt?: string;
}
