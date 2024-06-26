package nl.rikp.customerService.mapper;

import nl.rikp.customerService.dto.RecipeRatingResponse;
import nl.rikp.customerService.model.RecipeRating;
import org.springframework.stereotype.Component;

/**
 * Component for mapping between RecipeRating entities and DTOs.
 */
@Component
public class RecipeRatingMapper {

    /**
     * Converts a RecipeRating entity to a RecipeRatingResponse.
     *
     * @param recipeRating the recipe rating entity
     * @return the recipe rating response DTO
     */
    public RecipeRatingResponse fromRecipeRating(RecipeRating recipeRating) {
        return new RecipeRatingResponse(
                recipeRating.getId(),
                recipeRating.getRecipeId(),
                recipeRating.getRating().getRating()
        );
    }
}
