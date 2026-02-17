import { useQueryClient } from "@tanstack/react-query";
import { workoutDayService } from "../../services/WorkoutDayService";
import { WorkoutExerciseVM } from "../../types/WorkoutExercise/types";

export const useWorkoutSessionEditor = (
  workoutDayId?: number,
  exercises?: WorkoutExerciseVM[],
  setExercises?: React.Dispatch<React.SetStateAction<WorkoutExerciseVM[]>>
) => {
  const queryClient = useQueryClient();

  const addExercise = async (exercise: { id: string; name: string }) => {
    if (!workoutDayId || !exercises || !setExercises) return;

    const exerciseId = Number(exercise.id);

    const exists = exercises.some(
      ex => Number(ex.id) === exerciseId || ex.name === exercise.name
    );

    if (exists) return false
    

    const workoutDay = await workoutDayService.getById(workoutDayId);

    const newExercises = [
      ...workoutDay.exercises,
      {
        exerciseId,
        sets: 3,
        reps: 10,
        weight: 0,
        restTimeSeconds: 60,
        exerciseOrder: workoutDay.exercises.length,
      },
    ];

    await workoutDayService.update(workoutDayId, {
      name: workoutDay.name,
      description: workoutDay.description,
      exercises: newExercises,
    });

    const newExerciseVM: WorkoutExerciseVM = {
      id: String(exerciseId),
      name: exercise.name,
      lastPerformance: "",
      sets: Array.from({ length: 3 }).map(() => ({
        id: crypto.randomUUID(),
        kg: "",
        reps: "",
        done: false,
      })),
    };

    setExercises(prev => [...prev, newExerciseVM]);

    queryClient.invalidateQueries({
      queryKey: ["workout-day", workoutDayId],
    });
  };

  const removeExercise = async (exerciseId: string) => {
    if (!workoutDayId || !exercises || !setExercises) return;

    const index = exercises.findIndex(ex => ex.id === exerciseId);
    if (index === -1) return;

    const workoutDay = await workoutDayService.getById(workoutDayId);

    const newExercises = workoutDay.exercises
      .filter((_, i) => i !== index)
      .map((ex, i) => ({ ...ex, exerciseOrder: i }));

    await workoutDayService.update(workoutDayId, {
      name: workoutDay.name,
      description: workoutDay.description,
      exercises: newExercises,
    });

    setExercises(prev => prev.filter(ex => ex.id !== exerciseId));

    queryClient.invalidateQueries({
      queryKey: ["workout-day", workoutDayId],
    });
  };

  return {
    addExercise,
    removeExercise,
  };
};
