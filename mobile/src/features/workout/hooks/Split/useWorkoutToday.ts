import { useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { Alert } from "react-native";
import { splitService } from "../../services/WorkoutSplitService";
import { WorkoutDayResponse, SplitResponse } from "~/types";

const ACTIVE_SPLIT_KEY = ["splits", "active"];
const WORKOUT_TODAY_KEY = ["workout", "today"];

const getCurrentDayOfWeek = () => {
  const jsDay = new Date().getDay();
  return jsDay === 0 ? 7 : jsDay;
};

export const useWorkoutToday = () => {
  const activeSplitQuery = useQuery<SplitResponse[]>({
    queryKey: ACTIVE_SPLIT_KEY,
    queryFn: splitService.getActive,
  });

  const activeSplit = activeSplitQuery.data?.[0] ?? null;

  const todayWorkoutQuery = useQuery<WorkoutDayResponse | null>({
    queryKey: [...WORKOUT_TODAY_KEY, activeSplit?.id],
    queryFn: async () => {
      const dayOfWeek = getCurrentDayOfWeek();
      return splitService.getWorkoutForDay(dayOfWeek);
    },
    enabled: !!activeSplit,
  });

  return {
    todayWorkout: todayWorkoutQuery.data ?? null,
    activeSplit,

    loading:
      activeSplitQuery.isLoading || todayWorkoutQuery.isLoading,

    reload: async () => {
      await Promise.all([
        activeSplitQuery.refetch(),
        todayWorkoutQuery.refetch(),
      ]);
    },
  };
};

