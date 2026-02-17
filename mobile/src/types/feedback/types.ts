export interface Feedback {
  id: number;
  userId: number;
  workoutSessionId: number;
  fatigueLevel: number;
  muscleSoreness: number;
  notes: string;
  createdAt: string;
}

export interface CreateFeedbackRequest {
  workoutSessionId: number;
  fatigueLevel: number;
  muscleSoreness: number;
  notes?: string;
}