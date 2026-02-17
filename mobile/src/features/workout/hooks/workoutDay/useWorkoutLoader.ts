import { useEffect, useState, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import { workoutDayService } from "../../services/WorkoutDayService";
import { WorkoutExerciseVM } from "../../types/WorkoutExercise/types";
import { WorkoutDayResponse } from "~/types";
import { useExercises } from "../Exercise/useWorkoutExercise";

const WORKOUT_DAY_KEY = (id?: number) => ["workout-day", id];

export const useWorkoutLoader = (workoutDayId?: number) => {
  const [exercises, setExercises] = useState<WorkoutExerciseVM[]>([]);
  const { exercises: availableExercises } = useExercises();

  const exerciseMap = useMemo(() => {
    const map = new Map<number, string>();
    availableExercises.forEach(ex => {
      map.set(ex.id, ex.name);
    });
    return map;
  }, [availableExercises]);

  const workoutQuery = useQuery<WorkoutDayResponse>({
    queryKey: WORKOUT_DAY_KEY(workoutDayId),
    queryFn: () => workoutDayService.getById(workoutDayId!),  
    enabled: !!workoutDayId,
  });

  useEffect(() => {
    if (!workoutQuery.data) return;
    // Aqui deve ser feito a extração para WorkoutExerciseResponse
    const formatted: WorkoutExerciseVM[] =
      workoutQuery.data.exercises.map(ex => ({
        id: String(ex.id),
        name: exerciseMap.get(ex.exerciseId) || `Exercício ${ex.exerciseId}`,
        lastPerformance: "",
        sets: Array.from({ length: ex.sets ?? 1 }).map(() => ({
          id: crypto.randomUUID(),
          kg: "",
          reps: "",
          done: false,
        })),
      }));

    setExercises(formatted);
  }, [workoutQuery.data, exerciseMap]);

  return {
    exercises,
    setExercises,
    loading: workoutQuery.isLoading,
    refetch: workoutQuery.refetch,
  };
};
