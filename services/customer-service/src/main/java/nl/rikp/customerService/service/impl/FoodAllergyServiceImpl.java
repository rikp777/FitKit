package nl.rikp.customerService.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.exception.FoodAllergyAlreadyExistsException;
import nl.rikp.customerService.exception.notFound.FoodAllergyNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.model.FoodAllergy;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.service.FoodAllergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class FoodAllergyServiceImpl extends CustomerBaseService implements FoodAllergyService {

    @Autowired
    public FoodAllergyServiceImpl(CustomerRepository repo, CustomerMapper mapper) {
        super(repo, mapper);
    }

    public CustomerResponse addAllergyToCustomer(Long customerId, Long foodAllergyId) {
        log.info("Adding food allergy for customer ID {} and food item ID {}.", customerId, foodAllergyId);
        var customer = getCustomer(customerId);

        var existingFoodAllergy = customerRepo.findByFoodAllergyId(customerId, foodAllergyId);
        if (existingFoodAllergy.isPresent()) {
            log.error("Food allergy already exists for customer ID {} and food item ID {}.", customerId, foodAllergyId);
            throw new FoodAllergyAlreadyExistsException("Food allergy already exists for customer ID " + customerId + " and food item ID " + foodAllergyId);
        }

        var foodAllergy = FoodAllergy.builder()
                .customer(customer)
                .foodAllergyId(foodAllergyId)
                .build();
        customer.addFoodAllergy(foodAllergy);

        var updatedCustomer = customerRepo.save(customer);
        log.info("Customer updated successfully.");
        return customerMapper.fromCustomer(updatedCustomer);
    }

    public void removeAllergyFromCustomer(Long customerId, Long foodAllergyId) {
        log.info("Removing food allergy for customer ID {} and food item ID {}.", customerId, foodAllergyId);
        var customer = getCustomer(customerId);
        var foodAllergy = getFoodAllergy(customerId, foodAllergyId);

        customer.removeFoodAllergy(foodAllergy);

        customerRepo.save(customer);
        log.info("Customer updated successfully.");
    }

    protected FoodAllergy getFoodAllergy(Long customerId, Long foodAllergyId) {
        return customerRepo.findByFoodAllergyId(customerId, foodAllergyId)
                .orElseThrow(() -> {
                    log.error("No food allergy found for customer ID {} and food item ID {}.", customerId, foodAllergyId);
                    return new FoodAllergyNotFoundException(
                            format("No food allergy found for customer ID:: %s and food item ID:: %s", customerId, foodAllergyId)
                    );
                });
    }
}
