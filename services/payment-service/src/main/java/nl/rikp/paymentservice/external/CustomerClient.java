package nl.rikp.paymentservice.external;

import nl.rikp.paymentservice.dto.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}"
)
public interface CustomerClient {

    @GetMapping("/{customerId}")
    Customer getCustomer(@PathVariable("customerId") Long customerId);
}
