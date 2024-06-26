package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.CustomerResponse;

public interface FoodAllergyService {
    CustomerResponse addAllergyToCustomer(Long customerId, Long foodAllergyId);
    void removeAllergyFromCustomer(Long customerId, Long foodAllergyId);
}
