package nl.rikp.notificationService.model;

import jakarta.persistence.*;
import nl.rikp.notificationService.kafka.OrderConfirmation;
import nl.rikp.notificationService.kafka.PaymentConfirmation;

import java.time.LocalDateTime;

@Entity
@Table
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private NotificationType type;
    private LocalDateTime notificationDate;

    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;
}
