package nl.rikp.paymentservice.controller;

import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.rikp.paymentservice.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final SubscriptionService subscriptionService;

    @PostMapping("/api/stripe/webhook")
    public ResponseEntity<String> handle(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }

        if ("customer.subscription.deleted".equals(event.getType())) {
            Subscription stripeSubscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
            if (stripeSubscription != null) {
                String stripeSubscriptionId = stripeSubscription.getId();
                subscriptionService.handleSubscriptionCancellation(stripeSubscriptionId);
            }
        }

        return ResponseEntity.ok("Received");
    }
}
