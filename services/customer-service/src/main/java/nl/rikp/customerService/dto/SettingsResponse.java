package nl.rikp.customerService.dto;

import nl.rikp.customerService.model.Language;
import nl.rikp.customerService.model.PreferredMetric;

public record SettingsResponse(
        boolean useDarkMode,
        PreferredMetric preferredMetric,
        boolean notificationsEnabled,
        Language language,
        int itemsPerPage
) {
}
