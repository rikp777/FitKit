package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.model.FoodItemPreferenceType;
import nl.rikp.customerService.model.FoodItemPriorityLevel;

public interface FoodItemService {
    CustomerResponse addFoodItemToCustomer(Long customerId, Long foodItemId, FoodItemPreferenceType preferenceType, FoodItemPriorityLevel foodItemPriorityLevel);
    CustomerResponse updateCustomerFoodItem(Long customerId, Long foodItemId, FoodItemPreferenceType preferenceType, FoodItemPriorityLevel foodItemPriorityLevel);
    void removeFoodItemFromCustomer(Long customerId, Long foodItemId);
}
