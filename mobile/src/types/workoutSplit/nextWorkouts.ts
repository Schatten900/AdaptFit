import { WorkoutDayResponse } from '../index';

export interface NextWorkoutsResponse {
  todayWorkout: WorkoutDayResponse | null;
  todayStatus: 'WORKOUT' | 'REST' | 'COMPLETED';
  nextWorkouts: WorkoutDayResponse[];
  currentDayOfWeek: number;
}
