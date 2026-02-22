export interface WorkoutExerciseVM {
  id: string;
  exerciseId?: number;
  name: string;
  lastPerformance?: string;
  sets: {
    id: string;
    kg: string;
    reps: string;
    done: boolean;
  }[];
}
