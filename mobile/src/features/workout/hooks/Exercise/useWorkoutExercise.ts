import { useState, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import { exerciseService } from "../../services/ExercisesCatalogService";
import { ExerciseResponse } from "~/types";

export interface ExerciseDTO {
  id: string;
  name: string;
  muscleGroup: string;
}

const EXERCISES_QUERY_KEY = ["exercises"];

export const useExercises = () => {
  // 🔹 filtros de UI
  const [searchText, setSearchText] = useState("");
  const [muscleGroup, setMuscleGroup] = useState<string | null>(null);

  // 🔹 QUERY
  const query = useQuery<ExerciseResponse[]>({
    queryKey: EXERCISES_QUERY_KEY,
    queryFn: exerciseService.getAll,
    staleTime: 1000 * 60 * 10, // 10 min (catálogo quase não muda)
  });

  // 🔹 FILTRAGEM DERIVADA
  const filteredExercises = useMemo(() => {
    if (!query.data) return [];

    return query.data.filter(ex => {
      const matchesText = ex.name
        .toLowerCase()
        .includes(searchText.toLowerCase());

      const matchesGroup = muscleGroup
        ? ex.muscleGroup === muscleGroup
        : true;

      return matchesText && matchesGroup;
    });
  }, [query.data, searchText, muscleGroup]);

  // 🔹 DTO
  const exercisesDTO: ExerciseDTO[] = useMemo(
    () =>
      filteredExercises.map(ex => ({
        id: String(ex.id),
        name: ex.name,
        muscleGroup: ex.muscleGroup,
      })),
    [filteredExercises]
  );

  return {
    // dados
    exercises: filteredExercises,
    exercisesDTO,

    // estados
    loading: query.isLoading,

    // ações de UI
    search: setSearchText,
    filterByGroup: setMuscleGroup,
    clearFilters: () => {
      setSearchText("");
      setMuscleGroup(null);
    },
  };
};