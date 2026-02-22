export interface ExerciseSummary {
  exerciseId: number;
  exerciseName: string;
  muscleGroup: string;
  sets?: number;
  reps?: number;
  weight?: number;
}

export interface HistoricoResponse {
  sessionId: number;
  workoutId: number;
  workoutName: string;
  sessionDate: string;
  durationMinutes?: number;
  notes?: string;
  exercises: ExerciseSummary[];
}

export interface EvolutionDataPoint {
  date: string;
  sessionsCount: number;
  totalDuration: number;
  totalReps: number;
  totalWeight: number;
  totalVolume: number;
  period: string;
}

export interface DashboardResponse {
  totalSessions: number;
  totalDurationMinutes: number;
  averageDuration: number;
  totalReps: number;
  totalWeight: number;
  totalVolume: number;
  sessionsByMuscleGroup: Record<string, number>;
  evolutionData: EvolutionDataPoint[];
  workoutDistribution: Record<string, number>;
}
