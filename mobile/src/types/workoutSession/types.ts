export interface SetPerformed {
  setNumber: number;
  reps: number;
  weight: number;
  done: boolean;
}

export interface ExercisePerformed {
  exerciseId: number;
  sets: SetPerformed[];
}

export interface CreateWorkoutSessionRequest {
  workoutDayId: number;
  sessionDate: string;
  durationMinutes: number;
  notes?: string;
  totalReps: number;
  totalWeight: number;
  totalVolume: number;
  exercises: ExercisePerformed[];
}

export interface SetPerformedResponse {
  setNumber: number;
  reps: number;
  weight: number;
  volume: number;
}

export interface ExercisePerformedResponse {
  exerciseId: number;
  exerciseName: string;
  muscleGroup: string;
  sets: SetPerformedResponse[];
  totalSets: number;
  totalReps: number;
  totalWeight: number;
  totalVolume: number;
}

export interface WorkoutSessionResponse {
  id: number;
  workoutDayId: number;
  workoutName: string;
  sessionDate: string;
  durationMinutes?: number;
  notes?: string;
  totalReps?: number;
  totalWeight?: number;
  totalVolume?: number;
  exercises?: ExercisePerformedResponse[];
}
