package nl.rikp.paymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "stripe_subscription_id", nullable = false)
    private String stripeSubscriptionId;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (!active) {
            expiryDate = LocalDateTime.now();
        }
    }

    public void cancel() {
        active = false;
    }
}
