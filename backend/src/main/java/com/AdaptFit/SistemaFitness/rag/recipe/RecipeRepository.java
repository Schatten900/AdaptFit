package com.AdaptFit.SistemaFitness.rag.recipe;

import com.AdaptFit.SistemaFitness.enums.DietType;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByRecipeId(String recipeId);

    List<Recipe> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT r FROM Recipe r WHERE r.caloriesPorPorcao BETWEEN :minCalories AND :maxCalories")
    List<Recipe> findByCaloriesRange(@Param("minCalories") Integer minCalories, @Param("maxCalories") Integer maxCalories);

    @Query("SELECT r FROM Recipe r WHERE r.proteinaG >= :minProtein")
    List<Recipe> findHighProtein(@Param("minProtein") Double minProtein);

    @Query("SELECT r FROM Recipe r WHERE :mealType MEMBER OF r.tiposRefeicao")
    List<Recipe> findByMealType(@Param("mealType") MealType mealType);

    @Query("SELECT r FROM Recipe r WHERE :dieta MEMBER OF r.dietas")
    List<Recipe> findByDiet(@Param("dieta") DietType dieta);

    @Query("SELECT r FROM Recipe r WHERE :objetivo MEMBER OF r.objetivos")
    List<Recipe> findByObjective(@Param("objetivo") GoalType objetivo);

    @Query("SELECT r FROM Recipe r WHERE :alergenio NOT MEMBER OF r.alergenios")
    List<Recipe> findByExcludingAllergen(@Param("alergenio") String alergenio);

    @Query("SELECT r FROM Recipe r WHERE r.alergenios IS EMPTY OR " +
           "(SELECT CASE WHEN COUNT(a) = 0 THEN 1 ELSE 0 END FROM r.alergenios a WHERE a IN :allergens) = 1")
    List<Recipe> findByExcludingAllergens(@Param("allergens") List<String> allergens);

    @Query("SELECT r FROM Recipe r WHERE r.nome LIKE %:keyword% OR r.ingredientes LIKE %:keyword%")
    List<Recipe> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT r FROM Recipe r WHERE r.recipeId IN :recipeIds")
    List<Recipe> findByRecipeIdIn(@Param("recipeIds") List<String> recipeIds);
}
