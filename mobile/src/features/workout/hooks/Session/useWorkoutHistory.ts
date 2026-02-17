import { useQuery } from "@tanstack/react-query";
import { workoutSessionService } from "../../services/WorkoutSessionService";
import { WorkoutSessionResponse } from "~/types";

const WORKOUT_HISTORY_KEY = ["workout-sessions"];

export const useWorkoutHistory = () => {
  const query = useQuery<WorkoutSessionResponse[]>({
    queryKey: WORKOUT_HISTORY_KEY,
    queryFn: workoutSessionService.getAll,
  });

  return {
    sessions: query.data ?? [],
    loading: query.isLoading,
    refetch: query.refetch,
  };
};
