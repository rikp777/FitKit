package nl.rikp.customerService.dto;

import nl.rikp.customerService.model.FoodItemPriorityLevel;

public record FoodItemResponse(
        Long id,
        FoodItemPriorityLevel level
) {

}
