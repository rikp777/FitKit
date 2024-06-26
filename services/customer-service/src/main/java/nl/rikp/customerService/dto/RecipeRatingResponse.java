package nl.rikp.customerService.dto;

public record RecipeRatingResponse(
        Long id,
        Long recipeId,
        Integer rating
) {
}
