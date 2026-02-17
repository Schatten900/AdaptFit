export interface ExerciseResponse {
  id: number;
  name: string;
  description?: string;
  muscleGroup: string;
  equipment?: string;
  isBodyweight: boolean;
  videoUrl?: string;
  imageUrl?: string;
  createdAt?: string;
}