package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.CustomerRequest;
import nl.rikp.customerService.dto.CustomerResponse;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Boolean doesCustomerExist(Long id);

    Page<CustomerResponse> getAllCustomers(int page, int size);
    CustomerResponse getCustomerById(Long id);

    CustomerResponse createNewCustomer(CustomerRequest request);
    CustomerResponse updateExistingCustomer(Long customerId, CustomerRequest request);
    void removeCustomer(Long id);
}
