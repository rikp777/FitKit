package nl.rikp.customerService.repository;

import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.FoodAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    @Query("""
            SELECT c
            FROM Customer c
            WHERE c.id = :customerId
            """)
    Optional<Customer> findById(
            @Param("customerId") Long customerId
    );

    @Query("""
            SELECT f
            FROM FoodAllergy f
            WHERE f.customer.id = :customerId
            AND f.foodAllergyId = :foodAllergyId
            """)
    Optional<FoodAllergy> findByFoodAllergyId(
            @Param("customerId") Long customerId,
            @Param("foodAllergyId") Long foodAllergyId
    );
}
