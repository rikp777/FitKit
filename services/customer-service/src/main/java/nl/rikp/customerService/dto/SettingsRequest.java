package nl.rikp.customerService.dto;

import jakarta.validation.constraints.*;

public record SettingsRequest(
        @NotNull(message = "Dark mode preference must not be null")
        boolean useDarkMode,

        @NotBlank(message = "Preferred metric must not be blank")
        @Pattern(regexp = "METRIC|IMPERIAL", message = "Preferred metric must be either 'METRIC' or 'IMPERIAL'")
        String preferredMetric,

        @NotNull(message = "Notifications enabled preference must not be null")
        boolean notificationsEnabled,

        @NotBlank(message = "Language must not be blank")
        @Pattern(regexp = "EN|NL|FR|DE|ES|IT|PT|RU|DA|SV|NO|FI|IS|TR|PL|CS|SK|HU|RO|BG|EL|HR|SR|SL|LT|LV|ET|MT|GA|CY|ZH|JA",
                message = "Language must be one of the supported language codes")
        String languageCode,

        @Min(value = 1, message = "Items per page must be at least 1")
        @Max(value = 100, message = "Items per page must not be more than 100")
        int itemsPerPage
) {
}
