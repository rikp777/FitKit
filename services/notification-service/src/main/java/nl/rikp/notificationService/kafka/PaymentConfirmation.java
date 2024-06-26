package nl.rikp.notificationService.kafka;

import nl.rikp.notificationService.dto.PaymentMethod;

import java.math.BigDecimal;

public record PaymentConfirmation(
    String orderReference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String customerEmail
) {
}
