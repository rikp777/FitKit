package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.RecipeRatingResponse;
import nl.rikp.customerService.model.RecipeRatingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RecipeRatingService {
    Page<RecipeRatingResponse> getAllRatingsForCustomer(Long customerId, Pageable pageable);
    RecipeRatingResponse getRatingByCustomerAndRecipe(Long customerId, Long recipeId);

    RecipeRatingResponse addNewRecipeRating(Long customerId, Long recipeId, RecipeRatingType recipeRatingType);
    RecipeRatingResponse updateRecipeRating(Long customerId, Long recipeId, RecipeRatingType recipeRatingType);
    void removeRecipeRating(Long customerId, Long recipeId);
}
