import { useQuery } from '@tanstack/react-query';
import { historyService } from '../services/HistoryService';

export function useHistory(
  period: string = 'month',
  muscleGroup?: string,
  exerciseId?: number,
  workoutId?: number
) {
  return useQuery({
    queryKey: ['history', period, muscleGroup, exerciseId, workoutId],
    queryFn: () => historyService.getHistory(period, muscleGroup, exerciseId, workoutId),
  });
}

export function useDashboard(
  period: string = 'month',
  muscleGroup?: string,
  exerciseId?: number,
  workoutId?: number
) {
  return useQuery({
    queryKey: ['dashboard', period, muscleGroup, exerciseId, workoutId],
    queryFn: () => historyService.getDashboard(period, muscleGroup, exerciseId, workoutId),
  });
}
