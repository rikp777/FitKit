package nl.rikp.customerService.validation;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.client.FoodItemClient;
import nl.rikp.customerService.exception.FoodItemAlreadyExistsException;
import nl.rikp.customerService.exception.PremiumFeatureException;
import nl.rikp.customerService.exception.notFound.FoodItemNotFoundException;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.FoodItemPreferenceType;
import nl.rikp.customerService.model.PremiumLevel;
import nl.rikp.customerService.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class FoodItemValidation {
    private final FoodItemRepository foodItemRepo;
    private final FoodItemClient foodItemClient;

    public FoodItemValidation(
            FoodItemRepository foodItemRepo,
            FoodItemClient foodItemClient
    ) {
        this.foodItemRepo = foodItemRepo;
        this.foodItemClient = foodItemClient;
    }

    private int getMaxTotalPreferences(PremiumLevel premiumLevel) {
        if (premiumLevel == null) {
            return 2;
        }
        return switch (premiumLevel) {
            case PLATINUM -> 50;
            case GOLD -> 30;
            case SILVER -> 15;
            default -> 5;
        };
    }

    private int getMaxTotalDislikes(PremiumLevel premiumLevel) {
        if (premiumLevel == null) {
            return 3;
        }

        return switch (premiumLevel) {
            case PLATINUM -> 50;
            case GOLD -> 30;
            case SILVER -> 15;
            default -> 5;
        };
    }

    private int getMaxDailyUpdates(PremiumLevel premiumLevel) {
        if (premiumLevel == null) {
            return 12;
        }

        return switch (premiumLevel) {
            case PLATINUM -> 192;
            case GOLD -> 96;
            case SILVER -> 48;
            default -> 24;
        };
    }

    public boolean doesFoodItemExits(Long foodItemId) {
        return foodItemClient.existsById(foodItemId);
    }

    public void validateFoodItemExistence(Long foodItemId) {
        if (!doesFoodItemExits(foodItemId)) {
            log.error("Food item with ID {} does not exist.", foodItemId);
            throw new FoodItemNotFoundException(format("Food item with ID %d does not exist.", foodItemId));
        }
    }

    public void ensureDailyUpdateLimitNotExceeded(Customer customer) {
        Integer maxUpdatesPerDay  = getMaxDailyUpdates(customer.getPremiumLevel());
        Integer updatesToday  = foodItemRepo.countUpdatedTodayByCustomer(customer.getId());

        if (updatesToday >= maxUpdatesPerDay) {
            log.error("Customer ID {} with premium level {} has exceeded the maximum allowed number of updates for today. Max allowed: {}, Updates today: {}.",
                    customer.getId(), customer.getPremiumLevel(), maxUpdatesPerDay, updatesToday);
            throw new PremiumFeatureException(format("Customer ID %d has exceeded the maximum allowed number of updates for today. Max allowed: %d, Updates today: %d.",
                    customer.getId(), maxUpdatesPerDay, updatesToday));
        }
    }

    public void ensureMaxPreferencesLimitNotExceeded(Customer customer, FoodItemPreferenceType preferenceType) {
        int maxQuantity;
        String itemType;
        switch (preferenceType) {
            case PREFERRED -> {
                maxQuantity = getMaxTotalPreferences(customer.getPremiumLevel());
                itemType = "preferences";
            }
            case DISLIKED -> {
                maxQuantity = getMaxTotalDislikes(customer.getPremiumLevel());
                itemType = "dislikes";
            }
            default -> {
                return;
            }
        }

        int currentItemQuantity = foodItemRepo.findAllByFoodPreferenceType(customer.getId(), preferenceType).size();
        if (currentItemQuantity >= maxQuantity) {
            log.error("Customer ID {} has reached the limit of {} {}.", customer.getId(), maxQuantity, itemType);
            throw new PremiumFeatureException(format("Customer ID %s has reached the limit of %d %s.", customer.getId(), maxQuantity, itemType));
        }
    }

    public void validateUniqueFoodItem(Long customerId, Long foodItemId, FoodItemPreferenceType preferenceType) {
        boolean alreadyExists = foodItemRepo.existsByIdAndPreferenceType(customerId, foodItemId, preferenceType);
        if(alreadyExists){
            log.error("Food item with ID {} for customer ID {} already has preference type {}.", foodItemId, customerId, preferenceType);
            throw new FoodItemAlreadyExistsException(format("Food item with ID %d for customer ID %d already has preference type %s.", foodItemId, customerId, preferenceType));
        }
    }
}
