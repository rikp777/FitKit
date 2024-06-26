package nl.rikp.customerService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a food item associated with a customer.
 *
 * This entity holds information about the food item, including its priority level and preference type,
 * as well as related metadata such as creation and update dates. It maintains a many-to-one relationship
 * with the Customer entity and includes various constraints and indexes to ensure data integrity and optimize
 * query performance.
 */
//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(
        name = "food_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="unique_customer_id_food_item_id",
                        columnNames = {"customer_id", "food_item_id"}
                )
        },
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id"),
                @Index(name = "idx_customer_food_item", columnList = "customer_id, food_item_id")
        }
)
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "food_item_id")
    private Long foodItemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level")
    @NotNull(message = "Priority level cannot be null")
    private FoodItemPriorityLevel foodItemPriorityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type")
    @NotNull(message = "Preference type cannot be null")
    private FoodItemPreferenceType preferenceType;

    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "updated_count", nullable = false)
    private int updatedCount = 0;

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
     * Updates the priority level and preference type of the food item.
     *
     * @param foodItemPriorityLevel the new priority level
     * @param preferenceType the new preference type
     */
    public void update(
            FoodItemPriorityLevel foodItemPriorityLevel,
            FoodItemPreferenceType preferenceType
    ){
        this.foodItemPriorityLevel = foodItemPriorityLevel;
        this.preferenceType = preferenceType;
    }


    // 3
    // you did not tell what flowcontrol is <- did but very late
    // the intro is to long you have to tell more related to your project

    // 5
    // what where the improvements <- did but very late

    // use more anecdotes tell a story

    // poorly prepared, better prepare your presentation
    // it is not a coherent story
    // pre-record your demo
    // Takes a bit of a sloppy approach
    // Try to hold the attention of your listeners
    // Take this a learning moment
    // no scructure
}
