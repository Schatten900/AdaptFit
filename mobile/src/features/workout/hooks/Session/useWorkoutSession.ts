import { useMutation, useQueryClient } from "@tanstack/react-query";
import { workoutSessionService } from "../../services/WorkoutSessionService";
import { CreateWorkoutSessionRequest } from "~/types";

const WORKOUT_HISTORY_KEY = ["workout-sessions"];

export const useWorkoutSession = () => {
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (payload: CreateWorkoutSessionRequest) =>
      workoutSessionService.create(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: WORKOUT_HISTORY_KEY,
      });
    },
  });

  return {
    createSession: mutation.mutateAsync,
    loading: mutation.isPending,
  };
};
