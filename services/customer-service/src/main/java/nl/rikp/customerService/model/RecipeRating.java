package nl.rikp.customerService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a recipe rating given by a customer.
 *
 * This entity holds the rating type, customer information, and related metadata such as creation and update dates.
 * It maintains a many-to-one relationship with the Customer entity and includes various constraints and indexes
 * to ensure data integrity and optimize query performance.
 */
//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(
        name = "recipe_ratings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="unique_customer_id_recipe_id",
                        columnNames = {"customer_id", "recipe_id"}
                )
        },
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id"),
                @Index(name = "idx_customer_recipe", columnList = "customer_id, recipe_id")
        }
)
public class RecipeRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecipeRatingType rating;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "updated_count")
    private int updatedCount;

    /**
     * Sets the creation date and initializes the update date and update count when the entity is first persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
        this.updatedDate = LocalDate.now();
        this.updatedCount = 0;
    }

    /**
     * Updates the update date and manages the update count when the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        LocalDate today = LocalDate.now();
        if (!today.equals(this.updatedDate)) {
            this.updatedDate = today;
            this.updatedCount = 0;
        } else {
            this.updatedCount++;
        }
    }

    /**
     * Updates the rating type of the recipe rating.
     *
     * @param recipeRatingType the new rating type
     */
    public void update(RecipeRatingType recipeRatingType) {
        this.rating = recipeRatingType;
    }
}
