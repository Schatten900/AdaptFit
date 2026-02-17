import { useState, useEffect, useMemo } from "react";
import { WorkoutExerciseEditorVM } from "../../types/WorkoutDayEditor/types";
import { CreateWorkoutDayRequest, UpdateWorkoutDayRequest } from "~/types";
import { useWorkoutDays } from "./useWorkoutDays";
import { WorkoutDayResponse } from "~/types/workoutDay/types";
import { useExercises } from "../Exercise/useWorkoutExercise";

export const useWorkoutDayEditor = (initialWorkoutDayId?: number) => {
  const { createWorkoutDay, updateWorkoutDay } = useWorkoutDays();
  const { exercises: availableExercises } = useExercises();

  const exerciseMap = useMemo(() => {
    const map = new Map<number, string>();
    availableExercises.forEach(ex => {
      map.set(ex.id, ex.name);
    });
    return map;
  }, [availableExercises]);

  const [workoutDayId, setWorkoutDayId] = useState<number | undefined>(initialWorkoutDayId);
  const [workoutDayName, setWorkoutDayName] = useState("");
  const [workoutDayDescription, setWorkoutDayDescription] = useState("");
  const [exercises, setExercises] = useState<WorkoutExerciseEditorVM[]>([]);
  const [validationError, setValidationError] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(!!initialWorkoutDayId);

  const loadWorkoutDay = (workoutDay: WorkoutDayResponse) => {
    setWorkoutDayId(workoutDay.id);
    setWorkoutDayName(workoutDay.name);
    setWorkoutDayDescription(workoutDay.description || "");
    setIsEditing(true);

    if (workoutDay.exercises) {
      setExercises(
        workoutDay.exercises.map((ex, index) => ({
          exerciseId: ex.exerciseId,
          name: exerciseMap.get(ex.exerciseId) || `Exercício ${ex.exerciseId}`,
          sets: ex.sets ?? 3,
          reps: ex.reps ?? 10,
          weight: ex.weight ?? 0,
          restTimeSeconds: ex.restTimeSeconds ?? 60,
          exerciseOrder: index,
        }))
      );
    }
  };

  const addExercise = (exercise: WorkoutExerciseEditorVM) => {
    setExercises(prev => [...prev, exercise]);
  };

  const removeExercise = (exerciseId: number) => {
    setExercises(prev =>
      prev.filter(ex => ex.exerciseId !== exerciseId)
    );
  };

  const saveWorkoutDay = async () => {
    if (!workoutDayName.trim()) {
      setValidationError("Nome do treino é obrigatório");
      return;
    }

    setValidationError(null);

    if (isEditing && workoutDayId) {
      await updateWorkoutDay.mutateAsync({
        id: workoutDayId,
        data: {
          name: workoutDayName,
          description: workoutDayDescription,
          exercises: exercises.map((ex, index) => ({
            exerciseId: ex.exerciseId,
            sets: ex.sets,
            reps: ex.reps,
            weight: ex.weight,
            restTimeSeconds: ex.restTimeSeconds,
            exerciseOrder: index,
          })),
        },
      });
    } else {
      await createWorkoutDay.mutateAsync({
        name: workoutDayName,
        description: workoutDayDescription,
        exercises: exercises.map((ex, index) => ({
          exerciseId: ex.exerciseId,
          sets: ex.sets,
          reps: ex.reps,
          weight: ex.weight,
          restTimeSeconds: ex.restTimeSeconds,
          exerciseOrder: index,
        })),
      });
    }
};


  const clearForm = () => {
    setWorkoutDayId(undefined);
    setWorkoutDayName("");
    setWorkoutDayDescription("");
    setExercises([]);
    setValidationError(null);
    setIsEditing(false);
  };

  return {
    isEditing,
    workoutDayId,
    workoutDayName,
    setWorkoutDayName,
    workoutDayDescription,
    setWorkoutDayDescription,
    exercises,

    loadWorkoutDay,
    addExercise,
    removeExercise,
    saveWorkoutDay,
    clearForm,

    loading: createWorkoutDay.isPending || updateWorkoutDay.isPending,
    error: validationError ?? createWorkoutDay.error ?? updateWorkoutDay.error,
  };
};
