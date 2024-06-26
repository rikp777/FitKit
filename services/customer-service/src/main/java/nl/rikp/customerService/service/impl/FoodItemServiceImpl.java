package nl.rikp.customerService.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.exception.FoodPreferenceRemoveException;
import nl.rikp.customerService.exception.notFound.FoodPreferenceNotFoundException;
import nl.rikp.customerService.exception.notFound.RecipeRatingNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.model.FoodItem;
import nl.rikp.customerService.model.FoodItemPreferenceType;
import nl.rikp.customerService.model.FoodItemPriorityLevel;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.repository.FoodItemRepository;
import nl.rikp.customerService.service.FoodItemService;
import nl.rikp.customerService.validation.FoodItemValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class FoodItemServiceImpl extends CustomerBaseService implements FoodItemService {
    private final FoodItemRepository repo;
    private final FoodItemValidation validation;

    @Autowired
    public FoodItemServiceImpl(
            FoodItemValidation foodItemValidation,
            FoodItemRepository foodItemRepo,
            CustomerRepository repo,
            CustomerMapper mapper
    ) {
        super(repo, mapper);
        this.repo = foodItemRepo;
        this.validation = foodItemValidation;
    }

    @Transactional
    public CustomerResponse addFoodItemToCustomer(
            Long customerId,
            Long foodItemId,
            FoodItemPreferenceType preferenceType,
            FoodItemPriorityLevel foodItemPriorityLevel
    ) {
        validation.validateFoodItemExistence(foodItemId);

        log.info("Adding food preference for customer ID {} and food item ID {}.", customerId, foodItemId);
        var customer = getCustomer(customerId);

        validation.validateUniqueFoodItem(customerId, foodItemId, preferenceType);
        validation.ensureMaxPreferencesLimitNotExceeded(customer, preferenceType);

        var existingFoodItem = repo.findById(customerId, foodItemId);

        if (existingFoodItem.isPresent()) {
            existingFoodItem.get().update(foodItemPriorityLevel, preferenceType);
            log.info("Food preference updated successfully.");
        } else {
            var foodPreference = FoodItem.builder()
                    .customer(customer)
                    .foodItemId(foodItemId)
                    .foodItemPriorityLevel(foodItemPriorityLevel)
                    .preferenceType(preferenceType)
                    .build();
            customer.addFoodItem(foodPreference);
            log.info("Food preference added successfully.");
        }

        var updatedCustomer = customerRepo.save(customer);
        log.info("Customer updated successfully.");
        return customerMapper.fromCustomer(updatedCustomer);
    }

    @Transactional
    public CustomerResponse updateCustomerFoodItem(
            Long customerId,
            Long foodItemId,
            FoodItemPreferenceType preferenceType,
            FoodItemPriorityLevel foodItemPriorityLevel
    ) {
        if(!validation.doesFoodItemExits(foodItemId)){
            removeFoodItemFromCustomer(customerId, foodItemId);
            log.error("Food item {} does not exist. Food item preference removed.", foodItemId);
            throw new RecipeRatingNotFoundException(format("Food item %d does not exist. Food item preference removed.", foodItemId));
        }

        log.info("Updating food preference for customer ID {} and food item ID {}.", customerId, foodItemId);
        var customer = getCustomer(customerId);

        validation.ensureDailyUpdateLimitNotExceeded(customer);

        var foodItem = fetch(customerId, foodItemId);
        foodItem.update(foodItemPriorityLevel, preferenceType);
        repo.save(foodItem);

        log.info("Food preference updated successfully for customer ID {}.", customerId);
        return customerMapper.fromCustomer(getCustomer(customerId));
    }

    @Transactional
    public void removeFoodItemFromCustomer(Long customerId, Long foodItemId) {
        log.info("Removing food preference for customer ID {} and food item ID {}.", customerId, foodItemId);
        var customer = getCustomer(customerId);
        var foodItem = fetch(customerId, foodItemId);

        if(foodItem.getPreferenceType() == FoodItemPreferenceType.NEUTRAL) {
            log.error("Cannot remove neutral food preference.");
            throw new FoodPreferenceRemoveException("Cannot remove neutral food preference.");
        }

        customer.removeFoodItem(foodItem);

        customerRepo.save(customer);
        log.info("Customer updated successfully.");
    }

    protected FoodItem fetch(Long customerId, Long foodItemId) {
        return repo.findById(customerId, foodItemId)
                .orElseThrow(() -> {
                    log.error("No food preference found for customer ID {} and food item ID {}.", customerId, foodItemId);
                    return new FoodPreferenceNotFoundException(
                            format("No food preference found for customer ID:: %s and food item ID:: %s", customerId, foodItemId)
                    );
                });
    }
}
