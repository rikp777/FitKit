package nl.rikp.customerService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a food allergy associated with a customer.
 *
 * This entity holds information about the food allergy and maintains a many-to-one relationship
 * with the Customer entity. It includes various constraints and indexes to ensure data integrity
 * and optimize query performance.
 */
//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(
        name = "food_allergies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="unique_customer_id_food_allergy_id",
                        columnNames = {"customer_id", "food_allergy_id"}
                )
        },
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id"),
                @Index(name = "idx_customer_food_allergy", columnList = "customer_id, food_allergy_id")
        }
)
public class FoodAllergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "food_allergy_id")
    private Long foodAllergyId;
}
