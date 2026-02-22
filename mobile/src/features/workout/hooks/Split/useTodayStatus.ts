import { useQuery } from "@tanstack/react-query";
import { splitService } from "../../services/WorkoutSplitService";
import { TodayStatusResponse } from "~/types";

const TODAY_STATUS_KEY = ["workout", "todayStatus"];

export const useTodayStatus = () => {
  return useQuery<TodayStatusResponse>({
    queryKey: TODAY_STATUS_KEY,
    queryFn: () => splitService.getTodayStatus(),
    retry: false,
  });
};
