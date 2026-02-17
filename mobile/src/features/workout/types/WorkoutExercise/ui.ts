export interface WorkoutExerciseUI {
  exerciseId: number;
  name: string;
  sets: number;
  reps: number;
  weight: number;
  restTimeSeconds: number;
  exerciseOrder: number;
}

export interface WorkoutExerciseCardProps {
  exercise: WorkoutExerciseUI;
  onEdit?: () => void;
  onRemove?: () => void;
}
