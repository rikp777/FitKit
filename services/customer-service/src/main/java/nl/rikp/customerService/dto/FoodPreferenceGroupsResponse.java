package nl.rikp.customerService.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record FoodPreferenceGroupsResponse(
        List<FoodItemResponse> preferred,
        List<FoodItemResponse> disliked,
        List<FoodItemResponse> neutrals
) {
}
