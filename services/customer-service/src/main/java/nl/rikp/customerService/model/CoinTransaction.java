package nl.rikp.customerService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a coin transaction for a customer.
 */
// Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

// JPA
@Entity
@Table(
        name = "coin_transactions",
        indexes = {
            @Index(name = "idx_customer_id", columnList = "customer_id"),
            @Index(name = "idx_transaction_date", columnList = "transaction_date"),
            @Index(name = "idx_customer_transaction_date", columnList = "customer_id, transaction_date")
        }
)
public class CoinTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "balance_before", nullable = false)
    private int balanceBefore;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    public enum TransactionType {
        PURCHASE,
        EXPENDITURE
    }
}
