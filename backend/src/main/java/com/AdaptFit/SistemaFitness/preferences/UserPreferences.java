package com.AdaptFit.SistemaFitness.preferences;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
public class UserPreferences {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "available_equipment", columnDefinition = "json")
    private String availableEquipment;

    @Column(columnDefinition = "json")
    private String injuries;

    @Column(name = "exercise_blacklist", columnDefinition = "json")
    private String exerciseBlacklist;
}
