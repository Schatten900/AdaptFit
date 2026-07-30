export interface ExerciseResponse {
  id: number;
  name: string;
  description?: string;
  primaryMuscle: string;
  secondaryMuscles?: string[];
  isBodyweight: boolean;
  createdAt?: string;
}
