package com.AdaptFit.SistemaFitness.profile;
import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "UserProfiles")
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    private Integer age;

    @NotNull
    @Min(1)
    private Double height;

    @NotNull
    @Min(1)
    private Double weight;

    @Enumerated(EnumType.STRING)
    private GoalType goal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experience;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
