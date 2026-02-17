import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { workoutDayService } from "../../services/WorkoutDayService";
import {
  CreateWorkoutDayRequest,
  UpdateWorkoutDayRequest,
  WorkoutDayResponse,
} from "~/types";

const WORKOUT_DAYS_QUERY_KEY = ["workout-days"];

export function useWorkoutDays() {
  const queryClient = useQueryClient();

  // 🔹 GET ALL
  const query = useQuery<WorkoutDayResponse[]>({
    queryKey: WORKOUT_DAYS_QUERY_KEY,
    queryFn: workoutDayService.getAll,
  });

  // 🔹 CREATE
  const createMutation = useMutation({
    mutationFn: (payload: CreateWorkoutDayRequest) =>
      workoutDayService.create(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: WORKOUT_DAYS_QUERY_KEY,
      });
    },
  });

  // 🔹 UPDATE
  const updateMutation = useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number;
      data: UpdateWorkoutDayRequest;
    }) => workoutDayService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: WORKOUT_DAYS_QUERY_KEY,
      });
    },
  });

  // 🔹 DELETE
  const deleteMutation = useMutation({
    mutationFn: (id: number) => workoutDayService.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: WORKOUT_DAYS_QUERY_KEY,
      });
    },
  });

  return {
    workoutDays: query.data ?? [],
    isLoading: query.isLoading,
    isFetching: query.isFetching,

    refetch: query.refetch,

    createWorkoutDay: createMutation,
    updateWorkoutDay: updateMutation,
    deleteWorkoutDay: deleteMutation,
  };
}
