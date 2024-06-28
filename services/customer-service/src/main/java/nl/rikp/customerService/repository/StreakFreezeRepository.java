package nl.rikp.customerService.repository;

import jakarta.transaction.Transactional;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.StreakFreeze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface StreakFreezeRepository extends JpaRepository<StreakFreeze, Long> {

    @Query("""
            SELECT COUNT(sf) > 0
            FROM StreakFreeze sf
            WHERE sf.customer.id = :customerId
             """)
    boolean hasAvailableFreezes(
            @Param("customerId") Long customerId
    );

    @Query("""
            SELECT sf
            FROM StreakFreeze sf
            WHERE sf.customer.id = :customerId
            AND sf.dateUsed IS NULL
            ORDER BY sf.id ASC
            """)
    Optional<StreakFreeze> findFirstUnusedByCustomerId(
            @Param("customerId") Long customerId
    );

    @Transactional
    default void addFreezesToCustomer(Long customerId, int count) {
        if(count > 5) {
            throw new IllegalArgumentException("Cannot add more than 5 freezes at once");
        }
        for (int i = 0; i < count; i++) {
            StreakFreeze freeze = StreakFreeze.builder()
                    .customer(
                            Customer.builder()
                                    .id(customerId)
                                    .build()
                    )
                    .dateReceived(LocalDate.now())
                    .build();
            save(freeze);
        }
    }

    @Transactional
    default void useStreakFreezeForCustomer(Long customerId) {
        StreakFreeze freeze = findFirstUnusedByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("No available freezes"));

        freeze.use();
        save(freeze);
    }

    @Query("""
            SELECT COUNT(sf)
            FROM StreakFreeze sf
            WHERE sf.customer.id = :customerId
            AND sf.dateUsed IS NULL
            """)
    int countNonActivatedStreakFreezesByCustomerId(
            @Param("customerId") Long customerId
    );
}
