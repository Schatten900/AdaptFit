package com.AdaptFit.SistemaFitness.profile;

import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import com.AdaptFit.SistemaFitness.enums.Gender;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfileRequest {
    private BigDecimal weight;
    private BigDecimal height;
    private Integer age;
    private GoalType goal;
    private ExperienceLevel experience;
    private Gender gender;
    private Integer daysPerWeek;
    private Integer sessionDuration;
}
