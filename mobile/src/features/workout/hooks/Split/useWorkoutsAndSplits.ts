import { useQuery } from "@tanstack/react-query";
import { workoutDayService } from '../../services/WorkoutDayService';
import { splitService } from '../../services/WorkoutSplitService';
import { WorkoutDayResponse, SplitResponse } from '~/types';


const WORKOUT_DAYS_AVAILABLE_KEY = ["workout-days", "available"];
const SPLITS_KEY = ["splits"];

export const useWorkoutsAndSplits = () => {
  const workoutDaysQuery = useQuery<WorkoutDayResponse[]>({
    queryKey: WORKOUT_DAYS_AVAILABLE_KEY,
    queryFn: workoutDayService.getAvailable,
  });

  const splitsQuery = useQuery<SplitResponse[]>({
    queryKey: SPLITS_KEY,
    queryFn: splitService.getAll,
  });

  const loading = workoutDaysQuery.isLoading || splitsQuery.isLoading;

  const refetch = async () => {
    await Promise.all([
      workoutDaysQuery.refetch(),
      splitsQuery.refetch(),
    ]);
  };

  return {
    workoutDays: workoutDaysQuery.data ?? [],
    splits: splitsQuery.data ?? [],
    loading,
    refetch,
    isFetching:
      workoutDaysQuery.isFetching || splitsQuery.isFetching,
  };
};
