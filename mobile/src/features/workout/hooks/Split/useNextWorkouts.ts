import { useQuery } from "@tanstack/react-query";
import { splitService } from "../../services/WorkoutSplitService";
import { NextWorkoutsResponse } from "~/types";

const NEXT_WORKOUTS_KEY = ["workout", "nextWorkouts"];

export const useNextWorkouts = () => {
  return useQuery<NextWorkoutsResponse>({
    queryKey: NEXT_WORKOUTS_KEY,
    queryFn: () => splitService.getNextWorkouts(),
    retry: false,
  });
};
