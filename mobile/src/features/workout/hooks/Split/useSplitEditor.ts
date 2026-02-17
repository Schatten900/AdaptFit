import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { splitService } from "../../services/WorkoutSplitService";
import { SplitWorkoutDayVM } from "../../types/SplitEditor/types";
import { CreateSplitRequest, UpdateSplitRequest } from "~/types";
import { SplitResponse } from "~/types/workoutSplit/types";

const DAYS_OF_WEEK = [
  { key: "Seg", value: 1 },
  { key: "Ter", value: 2 },
  { key: "Qua", value: 3 },
  { key: "Qui", value: 4 },
  { key: "Sex", value: 5 },
  { key: "Sáb", value: 6 },
  { key: "Dom", value: 7 },
];

const SPLITS_KEY = ["splits"];
const ACTIVE_SPLIT_KEY = ["splits", "active"];
const WORKOUT_TODAY_KEY = ["workout", "today"];

export const useSplitEditor = (initialSplitId?: number) => {
  const queryClient = useQueryClient();

  // 🔹 UI STATE
  const [splitId, setSplitId] = useState<number | undefined>(initialSplitId);
  const [splitName, setSplitName] = useState("");
  const [splitDescription, setSplitDescription] = useState("");
  const [splitWorkoutDays, setSplitWorkoutDays] = useState<SplitWorkoutDayVM[]>([]);
  const [isEditing, setIsEditing] = useState(!!initialSplitId);

  // 🔹 MUTATIONS
  const createSplit = useMutation({
    mutationFn: (payload: CreateSplitRequest) =>
      splitService.create(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: SPLITS_KEY });
      queryClient.invalidateQueries({ queryKey: ACTIVE_SPLIT_KEY });
      queryClient.invalidateQueries({ queryKey: WORKOUT_TODAY_KEY });
    },
  });

  const updateSplit = useMutation({
    mutationFn: ({ id, payload }: { id: number; payload: UpdateSplitRequest }) =>
      splitService.update(id, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: SPLITS_KEY });
      queryClient.invalidateQueries({ queryKey: ACTIVE_SPLIT_KEY });
      queryClient.invalidateQueries({ queryKey: WORKOUT_TODAY_KEY });
    },
  });

  // 🔹 HELPERS
  const initializeWeekSplit = () => {
    setSplitWorkoutDays(
      DAYS_OF_WEEK.map((day, index) => ({
        dayOfWeek: day.value,
        dayOrder: index,
        workoutDayId: 0,
      }))
    );
  };

  const loadSplit = (split: SplitResponse) => {
    setSplitId(split.id);
    setSplitName(split.name);
    setSplitDescription(split.description || "");
    setIsEditing(true);
    
    // Mapear os dias existentes
    const existingDays = DAYS_OF_WEEK.map((day, index) => {
      const existing = split.splitWorkoutDays?.find(swd => swd.dayOfWeek === day.value);
      return {
        dayOfWeek: day.value,
        dayOrder: index,
        workoutDayId: existing?.workoutDayId || 0,
      };
    });
    setSplitWorkoutDays(existingDays);
  };

  const updateWorkoutDayForDay = (dayOfWeek: number, workoutDayId: number) => {
    setSplitWorkoutDays(prev =>
      prev.map(day =>
        day.dayOfWeek === dayOfWeek
          ? { ...day, workoutDayId }
          : day
      )
    );
  };

  const saveSplit = async () => {
    if (!splitName.trim()) {
      throw new Error("VALIDATION_SPLIT_NAME");
    }

    const validWorkoutDays = splitWorkoutDays
      .filter(day => day.workoutDayId > 0)
      .map((day, index) => ({
        ...day,
        dayOrder: index,
      }));

    if (isEditing && splitId) {
      // Modo edição - faz update
      const request: UpdateSplitRequest = {
        name: splitName,
        description: splitDescription,
        splitWorkoutDays: validWorkoutDays,
      };
      await updateSplit.mutateAsync({ id: splitId, payload: request });
    } else {
      // Modo criação - faz create
      const request: CreateSplitRequest = {
        name: splitName,
        description: splitDescription,
        splitWorkoutDays: validWorkoutDays,
      };
      await createSplit.mutateAsync(request);
    }
  };

  const clearForm = () => {
    setSplitId(undefined);
    setSplitName("");
    setSplitDescription("");
    setSplitWorkoutDays([]);
    setIsEditing(false);
  };

  return {
    DAYS_OF_WEEK,

    // UI State
    isEditing,
    splitId,
    splitName,
    setSplitName,
    splitDescription,
    setSplitDescription,
    splitWorkoutDays,

    // Functions
    initializeWeekSplit,
    loadSplit,
    updateWorkoutDayForDay,
    saveSplit,
    clearForm,

    // Estados do React Query
    loading: createSplit.isPending || updateSplit.isPending,
  };
};
