import api from '~/config/api';
import {
  CreateWorkoutSessionRequest,
  WorkoutSessionResponse,
} from '~/types';

export const workoutSessionService = {
  create(
    request: CreateWorkoutSessionRequest
  ): Promise<WorkoutSessionResponse> {
    return api.post('/workout-sessions', request);
  },

  getAll(): Promise<WorkoutSessionResponse[]> {
    return api.get('/workout-sessions');
  },

  getLatest(): Promise<WorkoutSessionResponse> {
    return api.get('/workout-sessions/latest');
  },

  delete(id: number): Promise<void> {
    return api.delete(`/workout-sessions/${id}`);
  },
};
