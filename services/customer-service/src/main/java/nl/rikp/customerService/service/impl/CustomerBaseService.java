package nl.rikp.customerService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.exception.notFound.CustomerNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.repository.CustomerRepository;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class CustomerBaseService {
    protected final CustomerRepository customerRepo;
    protected final CustomerMapper customerMapper;

    protected Customer getCustomer(Long customerId) {
        return customerRepo.findById(customerId)
                .orElseThrow(() -> {
                    log.error("No customer found with the provided ID {}.", customerId);
                    return new CustomerNotFoundException(
                            format("No customer found with the provided ID:: %s", customerId)
                    );
                });
    }


}
