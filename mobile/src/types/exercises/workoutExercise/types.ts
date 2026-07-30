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
  sets?: number;
  reps?: number;
  weight?: number;
  restTimeSeconds?: number;
  exerciseOrder?: number;
}
