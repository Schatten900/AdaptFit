import api from '~/config/api';
import {
  HistoricoResponse,
  DashboardResponse,
} from '~/types/history/types';

export const historyService = {
  getHistory(
    period: string = 'month',
    muscleGroup?: string,
    exerciseId?: number,
    workoutId?: number
  ): Promise<HistoricoResponse[]> {
    const params = new URLSearchParams();
    params.append('period', period);
    if (muscleGroup) {
      params.append('muscleGroup', muscleGroup);
    }
    if (exerciseId) {
      params.append('exerciseId', exerciseId.toString());
    }
    if (workoutId) {
      params.append('workoutId', workoutId.toString());
    }
    return api.get(`/workout-sessions/history?${params.toString()}`);
  },

  getDashboard(
    period: string = 'month',
    muscleGroup?: string,
    exerciseId?: number,
    workoutId?: number
  ): Promise<DashboardResponse> {
    const params = new URLSearchParams();
    params.append('period', period);
    if (muscleGroup) {
      params.append('muscleGroup', muscleGroup);
    }
    if (exerciseId) {
      params.append('exerciseId', exerciseId.toString());
    }
    if (workoutId) {
      params.append('workoutId', workoutId.toString());
    }
    return api.get(`/workout-sessions/dashboard?${params.toString()}`);
  },
};
