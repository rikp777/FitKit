package nl.rikp.customerService.repository;

import nl.rikp.customerService.dto.StreakResponse;
import nl.rikp.customerService.model.Streak;
import nl.rikp.customerService.model.StreakType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long> {
    @Query("""
            SELECT s
            FROM Streak s
            WHERE s.customer.id = :customerId
            AND (s.endDate IS NULL OR s.endDate <> CURRENT_DATE)
            """)
    List<Streak> findActiveStreaksByCustomerId(
            @Param("customerId") Long customerId
    );

    @Query("""
            SELECT s
            FROM Streak s
            WHERE s.endDate IS NULL
            OR s.endDate <> CURRENT_DATE
            """)
    Page<Streak> findActiveStreaks(Pageable pageable);

    default List<Streak> findActiveStreaksInBatches(int pageNumber, int pageSize) {
        Page<Streak> streakPage = this.findActiveStreaks(PageRequest.of(pageNumber, pageSize));
        return streakPage.getContent();
    }

    @Query("""
            SELECT s
            FROM Streak s
            WHERE s.customer.id = :customerId
            AND s.type = :streakType
            AND (s.endDate IS NULL OR s.endDate <> CURRENT_DATE)
            """)
    Optional<Streak> getStreaksByCustomerIdAndType(
            @Param("customerId") Long customerId,
            @Param("streakType") StreakType streakType
    );
}
