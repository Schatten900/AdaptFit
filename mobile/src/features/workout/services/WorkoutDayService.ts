import api from '~/config/api';
import {
  CreateWorkoutDayRequest,
  UpdateWorkoutDayRequest,
  WorkoutDayResponse,
} from '~/types';

export const workoutDayService = {
  getAll(): Promise<WorkoutDayResponse[]> {
    return api.get('/workout-days');
  },

  getAvailable(): Promise<WorkoutDayResponse[]> {
    return api.get('/workout-days/available');
  },

  getById(id: number): Promise<WorkoutDayResponse> {
    return api.get(`/workout-days/${id}`);
  },

  create(request: CreateWorkoutDayRequest): Promise<WorkoutDayResponse> {
    return api.post('/workout-days', request);
  },

  update(
    id: number,
    request: UpdateWorkoutDayRequest
  ): Promise<WorkoutDayResponse> {
    return api.put(`/workout-days/${id}`, request);
  },

  delete(id: number): Promise<void> {
    return api.delete(`/workout-days/${id}`);
  },
};
