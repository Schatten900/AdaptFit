package com.AdaptFit.SistemaFitness.rag.dto;

import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import com.AdaptFit.SistemaFitness.enums.DietType;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.enums.MealType;
import lombok.Data;

import java.util.List;

@Data
public class RecipeResponse {
    private Long id;
    private String recipeId;
    private String nome;
    private List<String> ingredientes;
    private String modoPreparo;
    private Integer caloriesPorPorcao;
    private Double proteinaG;
    private Double carboidratosG;
    private Double gordurasG;
    private List<MealType> tiposRefeicao;
    private List<DietType> dietas;
    private List<String> alergenios;
    private List<GoalType> objetivos;
    private String dificuldade;
    private Integer tempoMinuto;

    public static RecipeResponse fromEntity(Recipe recipe) {
        RecipeResponse response = new RecipeResponse();
        response.setId(recipe.getId());
        response.setRecipeId(recipe.getRecipeId());
        response.setNome(recipe.getNome());
        response.setIngredientes(recipe.getIngredientes());
        response.setModoPreparo(recipe.getModoPreparo());
        response.setCaloriesPorPorcao(recipe.getCaloriesPorPorcao());
        response.setProteinaG(recipe.getProteinaG());
        response.setCarboidratosG(recipe.getCarboidratosG());
        response.setGordurasG(recipe.getGordurasG());
        response.setTiposRefeicao(recipe.getTiposRefeicao());
        response.setDietas(recipe.getDietas());
        response.setAlergenios(recipe.getAlergenios());
        response.setObjetivos(recipe.getObjetivos());
        response.setDificuldade(recipe.getDificuldade());
        response.setTempoMinuto(recipe.getTempoMinuto());
        return response;
    }
}
