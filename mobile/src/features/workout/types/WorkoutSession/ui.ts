export interface WorkoutSetUI {
  id: string;
  kg: string;
  reps: string;
  done: boolean;
}

export interface WorkoutSessionExerciseUI {
  id: string;
  name: string;
  lastPerformance?: string;
  sets: WorkoutSetUI[];
}
