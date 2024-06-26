package nl.rikp.customerService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.rikp.customerService.dto.SettingsRequest;
import nl.rikp.customerService.dto.SettingsResponse;
import nl.rikp.customerService.model.Language;
import nl.rikp.customerService.model.PreferredMetric;
import nl.rikp.customerService.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customer-id}/settings")
@Validated
@RequiredArgsConstructor
public class SettingsController {
    private final SettingsService settingsService;

    @GetMapping()
    public ResponseEntity<SettingsResponse> getSettingsByCustomerId(
            @PathVariable("customer-id") Long customerId
    ) {
        SettingsResponse response = settingsService.getSettingsByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SettingsResponse> createSettings(
            @PathVariable("customer-id") Long customerId,
            @RequestBody @Valid SettingsRequest request
    ) {
        SettingsResponse response = settingsService.createSettingsForCustomer(customerId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/dark-mode/{use-dark-mode}")
    public ResponseEntity<SettingsResponse> updateDarkMode(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("use-dark-mode") boolean useDarkMode
    ) {
        SettingsResponse response = settingsService.updateDarkMode(customerId, useDarkMode);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/preferred-metrics/{preferred-metric}")
    public ResponseEntity<SettingsResponse> updatePreferredMetrics(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("preferred-metric") String preferredMetricValue
    ) {
        PreferredMetric preferredMetric = PreferredMetric.fromString(preferredMetricValue);
        SettingsResponse response = settingsService.updatePreferredMetrics(customerId, preferredMetric);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/notifications/{notifications-enabled}")
    public ResponseEntity<SettingsResponse> updateNotifications(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("notifications-enabled") boolean notificationsEnabled
    ) {
        SettingsResponse response = settingsService.updateNotifications(customerId, notificationsEnabled);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/language/{language-code}")
    public ResponseEntity<SettingsResponse> updateLanguage(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("language-code") String languageCode
    ) {
        Language language = Language.fromStringLangCode(languageCode);
        SettingsResponse response = settingsService.updateLanguage(customerId, language);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/items-per-page/{items-per-page}")
    public ResponseEntity<SettingsResponse> updateItemsPerPage(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("items-per-page") int itemsPerPage
    ) {
        SettingsResponse response = settingsService.updateItemsPerPage(customerId, itemsPerPage);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeSettingsFromCustomer(
            @PathVariable("customer-id") Long customerId
    ) {
        settingsService.removeSettingsFromCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
