export interface WorkoutExerciseEditorVM {
  exerciseId: number;
  name: string;
  sets: number;
  reps: number;
  weight?: number;
  restTimeSeconds?: number;
  exerciseOrder: number;
}
