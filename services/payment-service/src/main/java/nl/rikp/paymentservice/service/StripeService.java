package nl.rikp.paymentservice.service;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public Customer getCustomer(String customerId) throws Exception {
        return Customer.retrieve(customerId);
    }

    public String createCustomer(String email, String phone) throws Exception {
        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName("Jenny Rosen")
                        .build();

        try{
            Customer customer = Customer.create(params);
            return customer.getId();
        } catch (Exception e) {
            log.error("StripeException while creating customer: {}", e.getMessage(), e);
            throw new Exception("Unable to create customer with Stripe", e);
        }
    }

    public Subscription createSubscription(
            String stripeCustomerId,
            String priceId
    ) throws Exception {
        SubscriptionCreateParams params =
                SubscriptionCreateParams.builder()
                        .setCustomer(stripeCustomerId)
                        .addItem(
                                SubscriptionCreateParams.Item.builder()
                                        .setPrice(priceId)
                                        .build())
                        .build();

        return Subscription.create(params);
    }
}
