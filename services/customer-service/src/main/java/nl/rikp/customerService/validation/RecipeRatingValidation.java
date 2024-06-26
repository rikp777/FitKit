package nl.rikp.customerService.validation;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.client.RecipeClient;
import nl.rikp.customerService.exception.PremiumFeatureException;
import nl.rikp.customerService.exception.RecipeRatingAlreadyExistsException;
import nl.rikp.customerService.exception.notFound.RecipeNotFoundException;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.PremiumLevel;
import nl.rikp.customerService.model.RecipeRatingType;
import nl.rikp.customerService.repository.RecipeRatingRepository;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class RecipeRatingValidation {
    private final RecipeClient recipeClient;
    private final RecipeRatingRepository recipeRatingRepo;

    public RecipeRatingValidation(
            RecipeClient recipeClient,
            RecipeRatingRepository recipeRatingRepo
    ) {
        this.recipeClient = recipeClient;
        this.recipeRatingRepo = recipeRatingRepo;
    }

    private int getMaxTotalRatings(PremiumLevel premiumLevel) {
        if (premiumLevel == null) {
            return 25;
        }

        return switch (premiumLevel) {
            case PLATINUM -> 400;
            case GOLD -> 200;
            case SILVER -> 100;
            default -> 50;
        };
    }

    private int getMaxDailyCreateRatings(PremiumLevel premiumLevel) {
        if (premiumLevel == null) {
            return 1;
        }

        return switch (premiumLevel) {
            case PLATINUM -> 100;
            case GOLD -> 50;
            case SILVER -> 25;
            default -> 12;
        };
    }
    private int getMaxDailyUpdateRatings(PremiumLevel premiumLevel) {
        if (premiumLevel == null) {
            return 2;
        }

        return switch (premiumLevel) {
            case PLATINUM -> 150;
            case GOLD -> 75;
            case SILVER -> 36;
            default -> 18;
        };
    }

    public boolean doesRecipeExist(Long recipeId) {
        return recipeClient.existsById(recipeId);
    }

    public void validateRecipeExistence(Long recipeId) {
        if(!doesRecipeExist(recipeId)){
            log.error("Recipe with ID {} does not exist.", recipeId);
            throw new RecipeNotFoundException(format("Recipe with ID %d does not exist.", recipeId));
        }
    }

    public void validateUniqueRecipeRating(Long customerId, Long recipeId, RecipeRatingType recipeRatingType) {
        boolean alreadyExists = recipeRatingRepo.existsByIdAndRecipeRatingType(customerId, recipeId, recipeRatingType);
        if(alreadyExists){
            log.error("Recipe with ID {} for customer ID {} already has a rating of type {}.", recipeId, customerId, recipeRatingType);
            throw new RecipeRatingAlreadyExistsException(format("Recipe with ID %d for customer ID %d already has a rating of type %s.", recipeId, customerId, recipeRatingType));
        }
    }

    public void ensureDailyCreateRatingsLimitNotExceeded(Customer customer) {
        Integer maxCreatesPerDay = getMaxDailyCreateRatings(customer.getPremiumLevel());
        Integer createsToday = recipeRatingRepo.countRatingsCreatedTodayByCustomer(customer.getId());

        if (createsToday >= maxCreatesPerDay) {
            log.error("Customer ID {} with premium level {} has exceeded the maximum allowed number of created ratings for today. Max allowed: {}, Ratings today: {}.",
                    customer.getId(), customer.getPremiumLevel(), maxCreatesPerDay, createsToday);
            throw new PremiumFeatureException(format("Customer ID %d has exceeded the maximum allowed number of created ratings for today. Max allowed: %d, Ratings today: %d.",
                    customer.getId(), maxCreatesPerDay, createsToday));
        }
    }

    public void ensureDailyUpdateRatingsLimitNotExceeded(Customer customer) {
        Integer maxUpdatesPerDay = getMaxDailyUpdateRatings(customer.getPremiumLevel());
        Integer updatesToday = recipeRatingRepo.countRatingsUpdatedTodayByCustomer(customer.getId());

        if (updatesToday >= maxUpdatesPerDay) {
            log.error("Customer ID {} with premium level {} has exceeded the maximum allowed number of updated ratings for today. Max allowed: {}, Ratings today: {}.",
                    customer.getId(), customer.getPremiumLevel(), maxUpdatesPerDay, updatesToday);
            throw new PremiumFeatureException(format("Customer ID %d has exceeded the maximum allowed number of updated ratings for today. Max allowed: %d, Ratings today: %d.",
                    customer.getId(), maxUpdatesPerDay, updatesToday));
        }
    }

    public void ensureTotalRatingsLimitNotExceeded(Customer customer) {
        Integer maxRatings = getMaxTotalRatings(customer.getPremiumLevel());
        Integer currentRatings = recipeRatingRepo.countTotalRatingsByCustomer(customer.getId());

        if (currentRatings >= maxRatings) {
            log.error("Customer ID {} with premium level {} has exceeded the maximum allowed number of ratings. Max allowed: {}, Ratings total: {}.",
                    customer.getId(), customer.getPremiumLevel(), maxRatings, currentRatings);
            throw new PremiumFeatureException(format("Customer ID %d has exceeded the maximum allowed number of ratings. Max allowed: %d, Ratings total: %d.",
                    customer.getId(), maxRatings, currentRatings));
        }
    }
}
