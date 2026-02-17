export interface WorkoutExerciseRequest {
  exerciseId: number;
  sets?: number;
  reps?: number;
  weight?: number;
  restTimeSeconds?: number;
  exerciseOrder?: number;
}

export interface WorkoutExerciseResponse {
  id: number;
  exerciseId: number;
  name: string;
  description?: string;
  muscleGroup?: string;
  equipment?: string;
  isBodyweight?: boolean;
  sets?: number;
  reps?: number;
  weight?: number;
  restTimeSeconds?: number;
  exerciseOrder?: number;
}