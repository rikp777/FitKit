package nl.rikp.customerService.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.dto.RecipeRatingResponse;
import nl.rikp.customerService.exception.notFound.RecipeRatingNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.mapper.RecipeRatingMapper;
import nl.rikp.customerService.model.RecipeRating;
import nl.rikp.customerService.model.RecipeRatingType;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.repository.RecipeRatingRepository;
import nl.rikp.customerService.service.RecipeRatingService;
import nl.rikp.customerService.validation.RecipeRatingValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class RecipeRatingServiceImpl extends CustomerBaseService implements RecipeRatingService {
    private final RecipeRatingRepository repo;
    private final RecipeRatingValidation validation;
    private final RecipeRatingMapper mapper;

    public RecipeRatingServiceImpl(
            RecipeRatingRepository recipeRatingRepo,
            RecipeRatingValidation recipeRatingValidation,
            RecipeRatingMapper recipeRatingMapper,
            CustomerRepository repo,
            CustomerMapper mapper
    ) {
        super(repo, mapper);
        this.repo = recipeRatingRepo;
        this.validation = recipeRatingValidation;
        this.mapper = recipeRatingMapper;
    }

    public Page<RecipeRatingResponse> getAllRatingsForCustomer(Long customerId, Pageable pageable) {
        return repo.findAllPaginated(customerId, pageable)
                .map(mapper::fromRecipeRating);
    }

    public RecipeRatingResponse getRatingByCustomerAndRecipe(Long customerId, Long recipeId) {
        return mapper.fromRecipeRating(fetchRecipeRating(customerId, recipeId));
    }

    @Transactional
    public RecipeRatingResponse addNewRecipeRating(
            Long customerId,
            Long recipeId,
            RecipeRatingType recipeRatingType
    ) {
        validation.validateRecipeExistence(recipeId);

        log.info("Adding recipe rating by customer ID {} and recipe ID {}.", customerId, recipeId);
        var customer = getCustomer(customerId);

        validation.validateUniqueRecipeRating(customerId, recipeId, recipeRatingType);
        validation.ensureDailyCreateRatingsLimitNotExceeded(customer);
        validation.ensureTotalRatingsLimitNotExceeded(customer);

        var existingRecipeRating = repo.findById(customerId, recipeId);

        RecipeRating updatedRecipeRating;
        if (existingRecipeRating.isPresent()) {
            var originalRecipeRating = existingRecipeRating.get();
            originalRecipeRating.update(recipeRatingType);
            updatedRecipeRating = repo.save(originalRecipeRating);
            log.info("Recipe rating updated successfully for customer ID {} and recipe ID {}.", customerId, recipeId);
        } else {
            var newRecipeRating = RecipeRating.builder()
                    .customer(customer)
                    .recipeId(recipeId)
                    .rating(recipeRatingType)
                    .build();
            updatedRecipeRating = repo.save(newRecipeRating);
            log.info("Recipe rating added successfully for customer ID {} and recipe ID {}.", customerId, recipeId);
        }

        return mapper.fromRecipeRating(updatedRecipeRating);
    }

    @Transactional
    public RecipeRatingResponse updateRecipeRating(
            Long customerId,
            Long recipeId,
            RecipeRatingType recipeRatingType
    ) {
        if(!validation.doesRecipeExist(recipeId)){
            removeRecipeRating(customerId, recipeId);
            log.error("Recipe {} does not exist. Recipe rating removed.", recipeId);
            throw new RecipeRatingNotFoundException(format("Recipe %d does not exist. Recipe rating removed.", recipeId));
        }

        log.info("Updating recipe rating for customer ID {} and recipe ID {}.", customerId, recipeId);
        var customer = getCustomer(customerId);

        validation.ensureDailyUpdateRatingsLimitNotExceeded(customer);

        var recipeRating = fetchRecipeRating(customerId, recipeId);
        recipeRating.update(recipeRatingType);
        var updatedRecipeRating = repo.save(recipeRating);

        log.info("Recipe rating updated successfully for customer ID {}.", customerId);
        return mapper.fromRecipeRating(updatedRecipeRating);
    }

    @Transactional
    public void removeRecipeRating(Long customerId, Long recipeId) {
        log.info("Removing recipe rating for customer ID {} and recipe ID {}.", customerId, recipeId);
        var recipeRating = fetchRecipeRating(customerId, recipeId);

        repo.delete(recipeRating);
        log.info("Recipe rating removed successfully.");
    }


    protected RecipeRating fetchRecipeRating(Long customerId, Long recipeId) {
        return repo.findById(customerId, recipeId)
                .orElseThrow(() -> {
                    log.error("No recipe rating found for customer ID {} and recipe ID {}.", customerId, recipeId);
                    return new RecipeRatingNotFoundException(
                            format("No recipe rating found for customer ID:: %s and recipe ID:: %s", customerId, recipeId)
                    );
                });
    }
}
