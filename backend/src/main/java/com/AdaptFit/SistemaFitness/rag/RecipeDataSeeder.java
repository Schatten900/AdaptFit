package com.AdaptFit.SistemaFitness.rag;

import com.AdaptFit.SistemaFitness.enums.DietType;
import com.AdaptFit.SistemaFitness.enums.MealType;
import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import com.AdaptFit.SistemaFitness.rag.recipe.RecipeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RecipeDataSeeder {

    private final RecipeRepository recipeRepository;

    @Value("classpath:RAG/nutricao/receitas/receitas.json")
    private Resource receitasJson;

    @Bean
    public CommandLineRunner seedRecipes() {
        return args -> {

            if (recipeRepository.count() > 0) {
                log.info("Recipes already exist in database, skipping seed");
                return;
            }

            log.info("Seeding recipes from receitas.json...");

            ObjectMapper mapper = new ObjectMapper();

            JsonNode recipes = mapper.readTree(
                    receitasJson.getInputStream()
            );

            List<Recipe> recipeEntities = new ArrayList<>();

            for (JsonNode recipeNode : recipes) {

                if(!recipeNode.has("recipe_id")) {
                    log.warn("Recipe without recipe_id, skipping...");
                    continue;
                }

                Recipe recipe = new Recipe();

                recipe.setRecipeId(
                        recipeNode.get("recipe_id").asText()
                );

                recipe.setNome(
                        recipeNode.get("nome").asText()
                );


                if(recipeNode.has("calorias_por_porcao")) {
                    recipe.setCaloriesPorPorcao(
                            recipeNode.get("calorias_por_porcao").asInt()
                    );
                }


                if(recipeNode.has("proteina_g")) {
                    recipe.setProteinaG(
                            recipeNode.get("proteina_g").asDouble()
                    );
                }


                if(recipeNode.has("carboidratos_g")) {
                    recipe.setCarboidratosG(
                            recipeNode.get("carboidratos_g").asDouble()
                    );
                }


                if(recipeNode.has("gorduras_g")) {
                    recipe.setGordurasG(
                            recipeNode.get("gorduras_g").asDouble()
                    );
                }


                if(recipeNode.has("modo_preparo")) {
                    recipe.setModoPreparo(
                            recipeNode.get("modo_preparo").asText()
                    );
                }


                if(recipeNode.has("tipo_refeicao")) {

                    List<MealType> tiposRefeicao = new ArrayList<>();

                    recipeNode.get("tipo_refeicao").forEach(t -> {
                        tiposRefeicao.add(
                                parseMealType(t.asText())
                        );
                    });

                    recipe.setTiposRefeicao(
                            tiposRefeicao.stream().distinct().toList()
                    );
                }

                if(recipeNode.has("dietas")) {

                    List<DietType> dietas = new ArrayList<>();

                    recipeNode.get("dietas").forEach(d -> {
                        dietas.add(
                                parseDietType(d.asText())
                        );
                    });

                    recipe.setDietas(
                            dietas.stream().distinct().toList()
                    );
                }

                if(recipeNode.has("dificuldade")) {
                    recipe.setDificuldade(
                            recipeNode.get("dificuldade").asText()
                    );
                }

                if(recipeNode.has("tempo_minimo_min")) {
                    recipe.setTempoMinuto(
                            recipeNode.get("tempo_minimo_min").asInt()
                    );
                }

                recipeEntities.add(recipe);
            }

            // SALVAR NO BANCO
            recipeRepository.saveAll(recipeEntities);

            log.info("Seeded {} recipes to database", recipeEntities.size());

        };
    }


    // ==================================================
    // HELPERS JSON
    // ==================================================

    private String getText(JsonNode node, String field) {

        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }

        return null;
    }


    private Integer getInt(JsonNode node, String field) {

        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asInt();
        }

        return 0;
    }


    private Double getDouble(JsonNode node, String field) {

        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asDouble();
        }

        return 0.0;
    }


    // ==================================================
    // ENUM PARSERS
    // ==================================================

    private MealType parseMealType(String value) {

        return switch (value.toLowerCase()) {

            case "cafe",
                 "cafe_da_manha",
                 "café",
                 "café da manhã"
                    -> MealType.BREAKFAST;

            case "almoco",
                 "almoço"
                    -> MealType.LUNCH;

            case "jantar"
                    -> MealType.DINNER;

            case "lanche",
                 "lanche_medio"
                    -> MealType.SNACK;

            case "almoco_rapido"
                    -> MealType.QUICK_LUNCH;

            case "pre_treino",
                 "pré-treino",
                 "pretreino"
                    -> MealType.PRE_WORKOUT;

            case "pos_treino",
                 "pós-treino",
                 "postreino"
                    -> MealType.POST_WORKOUT;

            case "jantar_leve"
                    -> MealType.LIGHT_MEAL;

            case "jantar_rapido"
                    -> MealType.QUICK_MEAL;

            case "acompanhamento"
                    -> MealType.SIDE_DISH;

            case "sobremesa"
                    -> MealType.DESSERT;

            case "entrada"
                    -> MealType.APPETIZER;

            case "petisco_saudavel",
                 "lanche_pratico"
                    -> MealType.HEALTHY_SNACK;

            case "bebida"
                    -> MealType.BEVERAGE;

            case "lanche_pos_treino"
                    -> MealType.POST_WORKOUT_SNACK;

            default
                    -> MealType.SNACK;
        };
    }


    private DietType parseDietType(String value) {

        return switch (value.toLowerCase()) {

            case "onivoro",
                 "onívoro"
                    -> DietType.OMNIVORE;

            case "vegetariano",
                 "vegetarian"
                    -> DietType.VEGETARIAN;

            case "vegano",
                 "végano",
                 "vegan"
                    -> DietType.VEGAN;

            case "low-carb",
                 "lowcarb",
                 "low carb",
                 "low_carb"
                    -> DietType.LOW_CARB;

            case "alta_proteina"
                    -> DietType.HIGH_PROTEIN;

            case "baixa_caloria"
                    -> DietType.LOW_CALORIE;

            case "cetogenica"
                    -> DietType.KETOGENIC;

            case "detox"
                    -> DietType.DETOX;

            case "equilibrada"
                    -> DietType.BALANCED;

            case "paleo"
                    -> DietType.PALEO;

            case "pescetariano"
                    -> DietType.PESCATARIAN;

            case "rico_em_fibras"
                    -> DietType.HIGH_FIBER;

            case "sem_gluten"
                    -> DietType.GLUTEN_FREE;

            case "sem_laticinios"
                    -> DietType.DAIRY_FREE;

            case "sem_alcool"
                    -> DietType.ALCOHOL_FREE;

            case "omega-3"
                    -> DietType.OMEGA_3;

            case "pratico",
                 "lanche_pratico"
                    -> DietType.PRACTICAL;

            case "pre_treino",
                 "pré-treino",
                 "pretreino"
                    -> DietType.PRE_WORKOUT;

            case "pos_treino",
                 "pós-treino",
                 "postreino"
                    -> DietType.POST_WORKOUT;

            default
                    -> DietType.OMNIVORE;
        };
    }
}