export interface CreateWorkoutSessionRequest {
  workoutId: number;
  sessionDate: string;
  durationMinutes: number;
  notes?: string;
}


export interface WorkoutSessionResponse {
  id: number;
  workoutId: number;
  workoutName: string;
  sessionDate: string;
  durationMinutes?: number;
  notes?: string;
}
