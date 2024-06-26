package nl.rikp.customerService.mapper;

import nl.rikp.customerService.dto.*;
import nl.rikp.customerService.model.*;
import org.springframework.stereotype.Component;

/**
 * Component for mapping between Customer entities and DTOs.
 */
@Component
public class CustomerMapper {

    /**
     * Creates default settings for a given customer.
     *
     * @param customer the customer for whom the settings are being created
     * @return the default settings for the customer
     */
    private Settings createDefaultSettings(Customer customer) {
        return Settings.builder()
                .customer(customer)
                .preferredMetric(PreferredMetric.getDefaultMetricForLanguage(customer.getLanguage()))
                .useDarkMode(false)
                .notificationsEnabled(true)
                .language(customer.getLanguage())
                .itemsPerPage(10)
                .build();
    }

    /**
     * Converts a CustomerRequest to a Customer entity.
     *
     * @param request the customer request data
     * @return the customer entity
     */
    public Customer toCustomer(CustomerRequest request) {
        Language language = request.language() != null ? Language.fromStringLangCode(request.language()) : Language.EN;

        var customer = Customer.builder()
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .language(language)
                .build();

        customer.addSettings(createDefaultSettings(customer));

        return customer;
    }

    /**
     * Converts a Customer entity to a CustomerResponse.
     *
     * @param customer the customer entity
     * @return the customer response DTO
     */
    public CustomerResponse fromCustomer(Customer customer) {
        var foodPreference = new FoodPreferenceGroupsResponse(
                customer.getFoodItemPreferred().stream().map(this::fromFoodItem).toList(),
                customer.getFoodItemDisliked().stream().map(this::fromFoodItem).toList(),
                customer.getFoodItemNeutral().stream().map(this::fromFoodItem).toList()
        );

        var foodAllergies = customer.getFoodAllergies().stream().map(this::fromFoodAllergy).toList();

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                foodPreference,
                foodAllergies,
                customer.getSettings() != null ? new SettingsResponse(
                        customer.getSettings().isUseDarkMode(),
                        customer.getSettings().getPreferredMetric(),
                        customer.getSettings().isNotificationsEnabled(),
                        customer.getSettings().getLanguage(),
                        customer.getSettings().getItemsPerPage()
                ) : null
        );
    }

    /**
     * Converts a FoodItem entity to a FoodItemResponse.
     *
     * @param foodItem the food item entity
     * @return the food item response DTO
     */
    public FoodItemResponse fromFoodItem(FoodItem foodItem) {
        return new FoodItemResponse(
                foodItem.getId(),
                foodItem.getFoodItemPriorityLevel()
        );
    }

    /**
     * Converts a FoodAllergy entity to a FoodAllergyResponse.
     *
     * @param foodAllergy the food allergy entity
     * @return the food allergy response DTO
     */
    public FoodAllergyResponse fromFoodAllergy(FoodAllergy foodAllergy) {
        return new FoodAllergyResponse(
                foodAllergy.getId()
        );
    }
}
