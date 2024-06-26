package nl.rikp.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import nl.rikp.paymentservice.model.Subscription;
import nl.rikp.paymentservice.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{customer-id}/{price-id}")
    public ResponseEntity<Long> subscribe(
            @RequestParam("customer-id") Long customerId,
            @RequestParam("price-id") String priceId
    ) {
        try {
            Subscription subscription = subscriptionService.create(customerId, priceId);
            return ResponseEntity.ok(subscription.getId());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}
