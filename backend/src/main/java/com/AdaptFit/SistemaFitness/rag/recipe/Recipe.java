package com.AdaptFit.SistemaFitness.rag.recipe;

import com.AdaptFit.SistemaFitness.enums.DietType;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.enums.MealType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String recipeId;

    @Column(nullable = false, length = 255)
    private String nome;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_ingredientes", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingrediente")
    private List<String> ingredientes;

    @Column(name = "modo_preparo", length = 2000)
    private String modoPreparo;

    @Column(name = "calorias_por_porcao")
    private Integer caloriesPorPorcao;

    @Column(name = "proteina_g")
    private Double proteinaG;

    @Column(name = "carboidratos_g")
    private Double carboidratosG;

    @Column(name = "gorduras_g")
    private Double gordurasG;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_tipos_refeicao", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "tipo_refeicao")
    private List<MealType> tiposRefeicao;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_dietas", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "dieta")
    private List<DietType> dietas;

    @ElementCollection
    @CollectionTable(name = "recipe_alergenios", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "alergenio")
    private List<String> alergenios;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_objetivos", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "objetivo")
    private List<GoalType> objetivos;

    @Column(length = 50)
    private String dificuldade;

    @Column(name = "tempo_minuto")
    private Integer tempoMinuto;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
