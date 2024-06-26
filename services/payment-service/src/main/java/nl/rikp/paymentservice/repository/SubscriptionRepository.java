package nl.rikp.paymentservice.repository;

import nl.rikp.paymentservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByCustomerId(Long customerId);

    @Query("""
                SELECT s
                FROM Subscription s
                WHERE s.customerId = :customerId
                AND s.active = :active
            """)
    Optional<Subscription> findByCustomerIdAndActive(
            @Param("customerId") Long customerId,
            @Param("active") boolean active
    );

    @Query("""
                SELECT s
                FROM Subscription s
                WHERE s.customerId = :customerId
                ORDER BY s.createdDate DESC
            """)
    Optional<Subscription> findTopByCustomerIdOrderByCreatedDateDesc(
            @Param("customerId") Long customerId
    );

    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
