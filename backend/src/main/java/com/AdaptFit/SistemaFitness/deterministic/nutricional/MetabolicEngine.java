package com.AdaptFit.SistemaFitness.deterministic.nutricional;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLogRepository;
import com.AdaptFit.SistemaFitness.enums.ActivityLevel;
import com.AdaptFit.SistemaFitness.enums.AdjustmentReason;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.profile.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetabolicEngine {

    private final NutritionalPlanRepository nutritionalPlanRepository;
    private final EvolutionLogRepository evolutionLogRepository;

    private static final double PROTEIN_MIN_G_PER_KG = 1.6;
    private static final double PROTEIN_MAX_G_PER_KG = 2.2;
    private static final double FAT_MIN_G_PER_KG = 0.6;
    private static final double FAT_MAX_G_PER_KG = 1.0;

    private static final double CUTTING_CALORIE_DEFICIT_MIN = 0.10;
    private static final double CUTTING_CALORIE_DEFICIT_MAX = 0.20;
    private static final double BULKING_CALORIE_SURPLUS_MIN = 0.05;
    private static final double BULKING_CALORIE_SURPLUS_MAX = 0.15;

    public NutritionalPlan calculateAndSavePlan(Profile profile, ActivityLevel activityLevel) {
        double weightKg = profile.getWeight().doubleValue();
        double tmb = calculateBMR(profile);
        double tdee = calculateTDEE(tmb, activityLevel.getMultiplier());
        double targetCalories = applyGoalAdjustment(tdee, profile.getGoal());
        MacroDistribution macros = calculateMacros(weightKg, targetCalories, profile.getGoal());

        NutritionalPlan plan = nutritionalPlanRepository.findByUserIdAndIsActiveTrue(profile.getUserId())
                .orElse(new NutritionalPlan());

        plan.setUserId(profile.getUserId());
        plan.setTmb(roundToTwoDecimals(tmb));
        plan.setTdee(roundToTwoDecimals(tdee));
        plan.setTargetCalories(roundToTwoDecimals(targetCalories));
        plan.setProteinGrams(roundToTwoDecimals(macros.proteinGrams));
        plan.setCarbsGrams(roundToTwoDecimals(macros.carbsGrams));
        plan.setFatGrams(roundToTwoDecimals(macros.fatGrams));
        plan.setGoal(profile.getGoal());
        plan.setWeightKg(profile.getWeight());
        plan.setIsActive(true);

        NutritionalPlan savedPlan = nutritionalPlanRepository.save(plan);

        log.info("Nutritional plan created/updated for user {}: TMB={}, TDEE={}, Target={}", 
                profile.getUserId(), tmb, tdee, targetCalories);

        return savedPlan;
    }

    public double calculateBMR(Profile profile) {
        double weight = profile.getWeight().doubleValue();
        double height = profile.getHeight().doubleValue();
        int age = profile.getAge();

        if (profile.getGender() == null) {
            throw new ValidationException("Gênero é obrigatório para cálculo de TMB. Por favor, preencha seu perfil com o gênero.");
        }

        double bmr = switch (profile.getGender()) {
            case MASCULINE -> (10 * weight) + (6.25 * height) - (5 * age) + 5;
            case FEMININE -> (10 * weight) + (6.25 * height) - (5 * age) - 161;
            case OTHER -> 0.0;
        };

        return roundToTwoDecimals(bmr);
    }

    public double calculateTDEE(double bmr, double activityMultiplier) {
        return roundToTwoDecimals(bmr * activityMultiplier);
    }

    public double applyGoalAdjustment(double tdee, GoalType goal) {
        return switch (goal) {
            case FAT_LOSS -> {
                double deficit = (CUTTING_CALORIE_DEFICIT_MIN + CUTTING_CALORIE_DEFICIT_MAX) / 2;
                yield roundToTwoDecimals(tdee * (1 - deficit));
            }
            case MUSCLE_GAIN -> {
                double surplus = (BULKING_CALORIE_SURPLUS_MIN + BULKING_CALORIE_SURPLUS_MAX) / 2;
                yield roundToTwoDecimals(tdee * (1 + surplus));
            }
            case ENDURANCE, STRENGTH -> roundToTwoDecimals(tdee);
        };
    }

    public double getCuttingDeficitMin() {
        return CUTTING_CALORIE_DEFICIT_MIN;
    }

    public double getCuttingDeficitMax() {
        return CUTTING_CALORIE_DEFICIT_MAX;
    }

    public double getBulkingSurplusMin() {
        return BULKING_CALORIE_SURPLUS_MIN;
    }

    public double getBulkingSurplusMax() {
        return BULKING_CALORIE_SURPLUS_MAX;
    }

    public MacroDistribution calculateMacros(double weightKg, double targetCalories, GoalType goal) {
        double proteinGrams = weightKg * getProteinMultiplier(goal);
        proteinGrams = Math.max(proteinGrams, weightKg * PROTEIN_MIN_G_PER_KG);
        proteinGrams = Math.min(proteinGrams, weightKg * PROTEIN_MAX_G_PER_KG);

        double proteinCalories = proteinGrams * 4;

        double fatGrams = weightKg * getFatMultiplier(goal);
        fatGrams = Math.max(fatGrams, weightKg * FAT_MIN_G_PER_KG);
        fatGrams = Math.min(fatGrams, weightKg * FAT_MAX_G_PER_KG);

        double fatCalories = fatGrams * 9;

        double remainingCalories = targetCalories - proteinCalories - fatCalories;
        double carbsGrams = Math.max(0, remainingCalories / 4);

        return new MacroDistribution(
                roundToTwoDecimals(proteinGrams),
                roundToTwoDecimals(carbsGrams),
                roundToTwoDecimals(fatGrams)
        );
    }

    private double getProteinMultiplier(GoalType goal) {
        return switch (goal) {
            case MUSCLE_GAIN -> 2.0;
            case FAT_LOSS -> 2.2;
            case ENDURANCE -> 1.6;
            case STRENGTH -> 1.8;
        };
    }

    private double getFatMultiplier(GoalType goal) {
        return switch (goal) {
            case FAT_LOSS -> 0.8;
            case MUSCLE_GAIN -> 1.0;
            case ENDURANCE -> 0.8;
            case STRENGTH -> 0.9;
        };
    }

    public NutritionalPlan adjustCaloriesForWeightChange(Long userId, double currentWeight, int weeksWithoutProgress) {
        NutritionalPlan currentPlan = nutritionalPlanRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException("Nenhum plano nutricional ativo encontrado para o usuário"));

        double currentTarget = currentPlan.getTargetCalories();
        double previousTarget = currentTarget;

        if (currentPlan.getGoal() == GoalType.FAT_LOSS) {
            if (weeksWithoutProgress >= 2) {
                double newTarget = currentTarget * 0.95;
                currentPlan.setTargetCalories(roundToTwoDecimals(newTarget));
                logAdjustment(userId, AdjustmentReason.WEIGHT_LOSS_CUTTING, previousTarget, newTarget, "target_calories");
            }
        } else if (currentPlan.getGoal() == GoalType.MUSCLE_GAIN) {
            if (weeksWithoutProgress >= 2) {
                double newTarget = currentTarget * 1.05;
                currentPlan.setTargetCalories(roundToTwoDecimals(newTarget));
                logAdjustment(userId, AdjustmentReason.WEIGHT_GAIN_BULKING, previousTarget, newTarget, "target_calories");
            }
        }

        MacroDistribution macros = calculateMacros(currentWeight, currentPlan.getTargetCalories(), currentPlan.getGoal());
        currentPlan.setProteinGrams(macros.proteinGrams);
        currentPlan.setCarbsGrams(macros.carbsGrams);
        currentPlan.setFatGrams(macros.fatGrams);
        currentPlan.setWeightKg(BigDecimal.valueOf(currentWeight));

        return nutritionalPlanRepository.save(currentPlan);
    }

    private void logAdjustment(Long userId, AdjustmentReason reason, double previousValue, double newValue, String parameterName) {
        EvolutionLog evolutionLog = new EvolutionLog();
        evolutionLog.setUserId(userId);
        evolutionLog.setAdjustmentReason(reason);
        evolutionLog.setPreviousValue(previousValue);
        evolutionLog.setNewValue(newValue);
        evolutionLog.setParameterName(parameterName);
        evolutionLog.setDescription(reason.getDescription() + ": " + previousValue + " -> " + newValue);
        evolutionLogRepository.save(evolutionLog);

        log.info("Adjustment logged for user {}: {} - {} -> {}", userId, parameterName, previousValue, newValue);
    }

    public NutritionalPlan getActivePlan(Long userId) {
        return nutritionalPlanRepository.findByUserIdAndIsActiveTrue(userId)
                .orElse(null);
    }

    public NutritionalPlan getLatestPlan(Long userId) {
        return nutritionalPlanRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElse(null);
    }

    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public record MacroDistribution(double proteinGrams, double carbsGrams, double fatGrams) {}
}
