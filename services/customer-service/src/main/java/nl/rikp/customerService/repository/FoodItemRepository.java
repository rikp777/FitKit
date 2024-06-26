package nl.rikp.customerService.repository;

import nl.rikp.customerService.model.FoodItem;
import nl.rikp.customerService.model.FoodItemPreferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    @Query("""
        SELECT SUM(f.updatedCount)
        FROM FoodItem f
        WHERE f.customer.id = :customerId
        AND f.updatedDate = CURRENT_DATE
        """)
    Integer countUpdatedTodayByCustomer(
            @Param("customerId") Long customerId
    );

    @Query("""
            SELECT f
            FROM FoodItem f
            WHERE f.customer.id = :customerId
            AND f.foodItemId = :foodItemId
            AND f.preferenceType = :preferenceType
            """)
    Optional<FoodItem> findByIdAndFoodPreferenceType(
            @Param("customerId") Long customerId,
            @Param("foodItemId") Long foodItemId,
            @Param("preferenceType") FoodItemPreferenceType preferenceType
    );

    @Query("""
            SELECT f
            FROM FoodItem f
            WHERE f.customer.id = :customerId
            AND f.foodItemId = :foodItemId
            """)
    Optional<FoodItem> findById(
            @Param("customerId") Long customerId,
            @Param("foodItemId") Long foodItemId
    );

    @Query("""
            SELECT f
            FROM FoodItem f
            WHERE f.customer.id = :customerId
            AND f.preferenceType = :preferenceType
            """)
    List<FoodItem> findAllByFoodPreferenceType(
            @Param("customerId") Long customerId,
            @Param("preferenceType") FoodItemPreferenceType preferenceType
    );

    @Query("""
            SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
            FROM FoodItem f
            WHERE f.customer.id = :customerId
            AND f.foodItemId = :foodItemId
            AND f.preferenceType = :preferenceType
            """)
    boolean existsByIdAndPreferenceType(
            @Param("customerId") Long customerId,
            @Param("foodItemId") Long foodItemId,
            @Param("preferenceType") FoodItemPreferenceType preferenceType
    );
}
