import { WorkoutDayResponse, WorkoutSessionResponse } from '../index';

export type TodayStatus = 'WORKOUT' | 'REST' | 'COMPLETED';

export interface TodayStatusResponse {
  status: TodayStatus;
  workoutDay: WorkoutDayResponse | null;
  completedSession: WorkoutSessionResponse | null;
  message: string | null;
}
