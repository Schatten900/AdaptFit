import api from '~/config/api';
import { ExerciseResponse } from '~/types';

export const exerciseService = {
  getAll(): Promise<ExerciseResponse[]> {
    return api.get('/exercises');
  },

  getById(id: number): Promise<ExerciseResponse> {
    return api.get(`/exercises/${id}`);
  },
};
