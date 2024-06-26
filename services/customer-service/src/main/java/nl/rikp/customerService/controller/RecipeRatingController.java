package nl.rikp.customerService.controller;

import lombok.RequiredArgsConstructor;
import nl.rikp.customerService.dto.RecipeRatingResponse;
import nl.rikp.customerService.model.RecipeRatingType;
import nl.rikp.customerService.service.RecipeRatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customer-id}/recipe")
@RequiredArgsConstructor
public class RecipeRatingController {

    private final RecipeRatingService service;

    @GetMapping()
    public ResponseEntity<Page<RecipeRatingResponse>> getAllRecipeRating(
            @PathVariable("customer-id") Long customerId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        var pageable = PageRequest.of(page, size);
        var pageData = service.getAllRatingsForCustomer(customerId, pageable);

        return ResponseEntity.ok(pageData);
    }

    @GetMapping("/{recipe-id}")
    public ResponseEntity<RecipeRatingResponse> GetByIdRecipeRating(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("recipe-id") Long recipeId
    ){
        return ResponseEntity.ok(service.getRatingByCustomerAndRecipe(customerId, recipeId));
    }

    @PostMapping("/{recipe-id}/rating/{recipe-rating}")
    public ResponseEntity<RecipeRatingResponse> addRecipeRating(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("recipe-id") Long recipeId,
            @PathVariable("recipe-rating") int recipeRatingValue
    ){
        RecipeRatingType recipeRatingType = RecipeRatingType.fromValue(recipeRatingValue);
        var updatedData = service.addNewRecipeRating(customerId, recipeId, recipeRatingType);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(updatedData);
    }

    @PutMapping("/{recipe-id}/rating/{recipe-rating}")
    public ResponseEntity<RecipeRatingResponse> updateRecipeRating(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("recipe-id") Long recipeId,
            @PathVariable("recipe-rating") int recipeRatingValue
    ){
        RecipeRatingType recipeRatingType = RecipeRatingType.fromValue(recipeRatingValue);
        var updatedCustomer = service.updateRecipeRating(customerId, recipeId, recipeRatingType);

        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{recipe-id}")
    public ResponseEntity<Void> removeRecipeRating(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("recipe-id") Long recipeId
    ){
        service.removeRecipeRating(customerId, recipeId);
        return ResponseEntity.ok().build();
    }
}
