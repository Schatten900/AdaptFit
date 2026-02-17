import api from '~/config/api';
import {
  WorkoutExerciseRequest,
  WorkoutExerciseResponse,
} from '~/types';

export const workoutExerciseService = {
  addToWorkout(
    workoutDayId: number,
    request: WorkoutExerciseRequest
  ): Promise<WorkoutExerciseResponse> {
    return api.post(`/workout-days/${workoutDayId}/exercises`, request);
  },

  update(
    workoutExerciseId: number,
    request: WorkoutExerciseRequest
  ): Promise<WorkoutExerciseResponse> {
    return api.put(`/workout-exercises/${workoutExerciseId}`, request);
  },

  remove(workoutExerciseId: number): Promise<void> {
    return api.delete(`/workout-exercises/${workoutExerciseId}`);
  },

  reorder(
    workoutDayId: number,
    exercises: Pick<WorkoutExerciseRequest, 'exerciseOrder'>[]
  ): Promise<void> {
    return api.put(`/workout-days/${workoutDayId}/exercises/reorder`, {
      exercises,
    });
  },
};
