package nl.rikp.paymentservice.service;

import lombok.RequiredArgsConstructor;
import nl.rikp.paymentservice.dto.Customer;
import nl.rikp.paymentservice.external.CustomerClient;
import nl.rikp.paymentservice.model.Subscription;
import nl.rikp.paymentservice.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final StripeService stripeService;
    private final CustomerClient customerClient;
    private final SubscriptionRepository subscriptionRepository;

    public Subscription create(Long customerId, String priceId) throws Exception {
        // todo get customer
        Customer customer = customerClient.getCustomer(customerId);

        // todo check if user has active subscription
        // Check if user already has an active subscription
        Optional<Subscription> existingSubscription = subscriptionRepository.findByCustomerIdAndActive(customerId, true);
        if (existingSubscription.isPresent()) {
            throw new Exception("Customer already has an active subscription");
        }

        String stripeCustomerId = null;
        // Check if the customer already has a Stripe customer ID in the existing subscriptions
        Optional<Subscription> lastSubscription = subscriptionRepository.findTopByCustomerIdOrderByCreatedDateDesc(customerId);
        if (lastSubscription.isPresent()) {
            stripeCustomerId = lastSubscription.get().getStripeCustomerId();
        }

        // Create a new Stripe customer if one doesn't exist
        if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
            stripeCustomerId = stripeService.createCustomer(
                    customer.email(),
                    customer.phone()
            );
        }

        // Create the Stripe subscription
        com.stripe.model.Subscription stripeSubscription = stripeService.createSubscription(stripeCustomerId, priceId);

        // Save the subscription to your database
        Subscription subscription = Subscription.builder()
                .customerId(customerId)
                .stripeCustomerId(stripeCustomerId)
                .stripeSubscriptionId(stripeSubscription.getId())
                .active(true)
                .build();
        subscriptionRepository.save(subscription);

        return subscription;
    }

    public void handleSubscriptionCancellation(String stripeSubscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByStripeSubscriptionId(stripeSubscriptionId);
        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();
            subscription.cancel();
            subscriptionRepository.save(subscription);
        }
    }
}
