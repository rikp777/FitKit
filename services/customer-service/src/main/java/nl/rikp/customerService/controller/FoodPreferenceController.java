package nl.rikp.customerService.controller;

import lombok.RequiredArgsConstructor;
import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.model.FoodItemPreferenceType;
import nl.rikp.customerService.model.FoodItemPriorityLevel;
import nl.rikp.customerService.service.FoodAllergyService;
import nl.rikp.customerService.service.FoodItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customer-id}")
@Validated
@RequiredArgsConstructor
public class FoodPreferenceController {
    private final FoodAllergyService foodAllergyService;
    private final FoodItemService foodItemService;

    @PostMapping("/food-allergy/{food-allergy-id}")
    public ResponseEntity<CustomerResponse> addFoodAllergy(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("food-allergy-id") Long foodAllergyId
    ){
        var updatedCustomer = foodAllergyService.addAllergyToCustomer(customerId, foodAllergyId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(updatedCustomer);
    }
    @DeleteMapping("/food-allergy/{food-allergy-id}")
    public ResponseEntity<Void> removeFoodAllergy(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("food-allergy-id") Long foodAllergyId
    ){
        foodAllergyService.removeAllergyFromCustomer(customerId, foodAllergyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/food-preferences/{food-item-id}/level/{priority-level}")
    public ResponseEntity<CustomerResponse> addFoodPreference(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("food-item-id") Long foodItemId,
            @PathVariable("priority-level") int priorityLevelValue
    ){
        FoodItemPriorityLevel foodItemPriorityLevel = FoodItemPriorityLevel.fromValue(priorityLevelValue);
        FoodItemPreferenceType preferenceType = FoodItemPreferenceType.PREFERRED;

        var updatedCustomer = foodItemService.addFoodItemToCustomer(customerId, foodItemId, preferenceType, foodItemPriorityLevel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(updatedCustomer);
    }
    @PutMapping("/food-preferences/{food-item-id}/level/{priority-level}")
    public ResponseEntity<CustomerResponse> updateFoodPreference(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("food-item-id") Long foodItemId,
            @PathVariable("priority-level") int priorityLevelValue
    ){
        FoodItemPriorityLevel foodItemPriorityLevel = FoodItemPriorityLevel.fromValue(priorityLevelValue);
        FoodItemPreferenceType preferenceType = FoodItemPreferenceType.PREFERRED;

        var updatedCustomer = foodItemService.updateCustomerFoodItem(customerId, foodItemId, preferenceType, foodItemPriorityLevel);
        return ResponseEntity.ok(updatedCustomer);
    }


    @PostMapping("/food-dislikes/{food-item-id}/level/{priority-level}")
    public ResponseEntity<CustomerResponse> addFoodDislike(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("food-item-id") Long foodItemId,
            @PathVariable("priority-level") int priorityLevelValue
    ){
        FoodItemPriorityLevel foodItemPriorityLevel = FoodItemPriorityLevel.fromValue(priorityLevelValue);
        FoodItemPreferenceType preferenceType = FoodItemPreferenceType.DISLIKED;

        var updatedCustomer = foodItemService.addFoodItemToCustomer(customerId, foodItemId, preferenceType, foodItemPriorityLevel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(updatedCustomer);
    }
    @PutMapping("/food-dislikes/{food-item-id}/level/{priority-level}")
    public ResponseEntity<CustomerResponse> updateFoodDislike(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("food-item-id") Long foodItemId,
            @PathVariable("priority-level") int priorityLevelValue
    ){
        FoodItemPriorityLevel foodItemPriorityLevel = FoodItemPriorityLevel.fromValue(priorityLevelValue);
        FoodItemPreferenceType preferenceType = FoodItemPreferenceType.DISLIKED;

        var updatedCustomer = foodItemService.updateCustomerFoodItem(customerId, foodItemId, preferenceType, foodItemPriorityLevel);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/food-preference/{food-item-id}")
    public ResponseEntity<Void> removeFoodPreference(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("food-item-id") Long foodItemId
    ){
        foodItemService.removeFoodItemFromCustomer(customerId, foodItemId);
        return ResponseEntity.ok().build();
    }
}
