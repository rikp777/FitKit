package nl.rikp.customerService.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.dto.CustomerRequest;
import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.exception.notFound.CustomerNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class CustomerServiceImpl extends CustomerBaseService implements CustomerService {

    @Autowired
    public CustomerServiceImpl(CustomerRepository repo, CustomerMapper mapper) {
        super(repo, mapper);
    }

    public Boolean doesCustomerExist(Long id) {
        return customerRepo.existsById(id);
    }

    public Page<CustomerResponse> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepo.findAll(pageable)
                .map(customerMapper::fromCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {
        return customerRepo.findById(id)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("No customer found with the provided ID:: %s", id)
                ));
    }

    public CustomerResponse createNewCustomer(CustomerRequest request) {
        log.info("Creating customer with data: {}", request);
        var customer = customerRepo.save(customerMapper.toCustomer(request));

        log.info("Customer created successfully.");
        return customerMapper.fromCustomer(customer);
    }

    public CustomerResponse updateExistingCustomer(Long customerId, CustomerRequest request) {
        log.info("Updating customer with data: {}", request);
        var existingCustomer = getCustomer(customerId);

        existingCustomer.update(request);

        var updatedCustomer = customerRepo.save(existingCustomer);

        log.info("Customer updated successfully.");
        return customerMapper.fromCustomer(updatedCustomer);
    }

    public void removeCustomer(Long id) {
        var customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Cannot delete customer:: No customer found with the provided ID:: %s", id)
                ));

        customerRepo.delete(customer);
    }
}
