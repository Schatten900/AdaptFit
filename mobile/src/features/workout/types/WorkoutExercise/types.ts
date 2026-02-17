export interface WorkoutExerciseVM {
  id: string;
  name: string;
  lastPerformance?: string;
  sets: {
    id: string;
    kg: string;
    reps: string;
    done: boolean;
  }[];
}
