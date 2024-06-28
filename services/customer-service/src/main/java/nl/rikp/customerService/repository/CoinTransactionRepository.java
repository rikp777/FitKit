package nl.rikp.customerService.repository;

import nl.rikp.customerService.model.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {
    @Query("""
        SELECT ct 
        FROM CoinTransaction ct 
        WHERE ct.customer.id = :customerId
        """)
    List<CoinTransaction> findByCustomerId(
            @Param("customerId") Long customerId
    );

    @Query("""
        SELECT ct 
        FROM CoinTransaction ct 
        WHERE ct.customer.id = :customerId 
        AND ct.transactionDate 
        BETWEEN :startDate AND :endDate
        """)
    List<CoinTransaction> findByCustomerIdAndDateRange(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
