package nl.rikp.notificationService.kafka;

import nl.rikp.notificationService.dto.PaymentMethod;
import nl.rikp.notificationService.model.Customer;
import nl.rikp.notificationService.model.Product;

import java.math.BigDecimal;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        Customer customer,
        List<Product> products
) {
}
