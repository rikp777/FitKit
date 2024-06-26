package nl.rikp.customerService.mapper;

import nl.rikp.customerService.dto.SettingsRequest;
import nl.rikp.customerService.dto.SettingsResponse;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.Language;
import nl.rikp.customerService.model.PreferredMetric;
import nl.rikp.customerService.model.Settings;
import org.springframework.stereotype.Component;

/**
 * Component for mapping between Settings entities and DTOs.
 */
@Component
public class SettingsMapper {

    /**
     * Converts a Settings entity to a SettingsResponse.
     *
     * @param settings the settings entity
     * @return the settings response DTO
     */
    public SettingsResponse fromSettings(Settings settings) {
        return new SettingsResponse(
                settings.isUseDarkMode(),
                settings.getPreferredMetric(),
                settings.isNotificationsEnabled(),
                settings.getLanguage(),
                settings.getItemsPerPage()
        );
    }

    /**
     * Converts a SettingsRequest to a Settings entity.
     *
     * @param customer the customer associated with the settings
     * @param request the settings request data
     * @return the settings entity
     */
    public Settings toSettings(Customer customer, SettingsRequest request) {
        return Settings.builder()
                .customer(customer)
                .useDarkMode(request.useDarkMode())
                .preferredMetric(PreferredMetric.fromString(request.preferredMetric()))
                .notificationsEnabled(request.notificationsEnabled())
                .language(Language.fromStringLangCode(request.languageCode()))
                .itemsPerPage(request.itemsPerPage())
                .build();
    }
}
