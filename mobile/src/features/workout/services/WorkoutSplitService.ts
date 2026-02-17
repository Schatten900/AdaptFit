import api from '~/config/api';
import {
  SplitResponse,
  WorkoutDayResponse,
  CreateSplitRequest,
  UpdateSplitRequest
} from '~/types';

export const splitService = {
  getAll(): Promise<SplitResponse[]> {
    return api.get('/splits');
  },

  getById(id: number): Promise<SplitResponse> {
    return api.get(`/splits/${id}`);
  },

  getActive(): Promise<SplitResponse[]> {
    return api.get('/splits/active');
  },

  getWorkoutForDay(dayOfWeek: number): Promise<WorkoutDayResponse> {
    return api.get(`/splits/day/${dayOfWeek}`);
  },

  getWorkoutForToday(): Promise<WorkoutDayResponse> {
    return api.get('/splits/today');
  },

  create(request: CreateSplitRequest): Promise<void> {
    return api.post('/splits', request);
  },

  update(id: number, request: UpdateSplitRequest): Promise<void> {
    return api.put(`/splits/${id}`, request);
  },
};
