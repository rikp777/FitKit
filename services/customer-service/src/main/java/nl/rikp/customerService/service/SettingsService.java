package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.SettingsRequest;
import nl.rikp.customerService.dto.SettingsResponse;
import nl.rikp.customerService.model.Language;
import nl.rikp.customerService.model.PreferredMetric;

public interface SettingsService {
    SettingsResponse getSettingsByCustomerId(Long customerId);
    SettingsResponse createSettingsForCustomer(Long customerId, SettingsRequest request);
    SettingsResponse updateDarkMode(Long customerId, boolean useDarkMode);
    SettingsResponse updatePreferredMetrics(Long customerId, PreferredMetric preferredMetric);
    SettingsResponse updateNotifications(Long customerId, boolean notificationsEnabled);
    SettingsResponse updateLanguage(Long customerId, Language language);
    SettingsResponse updateItemsPerPage(Long customerId, int itemsPerPage);
    void removeSettingsFromCustomer(Long customerId);
}
