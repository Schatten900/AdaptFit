package com.AdaptFit.SistemaFitness.ia.context;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.deterministic.nutricional.NutritionalPlan;
import com.AdaptFit.SistemaFitness.deterministic.nutricional.NutritionalPlanRepository;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.ia.retriever.RecipeRetriever;
import com.AdaptFit.SistemaFitness.preferences.PreferencesRepository;
import com.AdaptFit.SistemaFitness.preferences.UserPreferences;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.AdaptFit.SistemaFitness.profile.ProfileRepository;
import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import com.AdaptFit.SistemaFitness.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NutritionContextBuilder {

    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final PreferencesRepository preferencesRepository;
    private final NutritionalPlanRepository nutritionalPlanRepository;
    private final RecipeRetriever recipeRetriever;

    public NutritionContext build(String userQuestion) {
        Long userId = userService.getCurrentUserId();

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Perfil não encontrado. Complete seu perfil primeiro."));

        UserPreferences preferences = preferencesRepository.findByUserId(userId).orElse(null);

        NutritionalPlan plan = nutritionalPlanRepository.findByUserIdAndIsActiveTrue(userId).orElse(null);

        GoalType goal = profile.getGoal();
        List<Recipe> ragDocuments = recipeRetriever.retrieveRecipes(userQuestion, null, goal, 10);

        log.info("Nutrition context built for user {}: profile={}, plan={}, rag={}",
                userId, profile != null, plan != null, ragDocuments.size());

        return NutritionContext.builder()
                .profile(profile)
                .preferences(preferences)
                .nutritionalPlan(plan)
                .ragDocuments(ragDocuments)
                .userQuestion(userQuestion)
                .build();
    }
}
