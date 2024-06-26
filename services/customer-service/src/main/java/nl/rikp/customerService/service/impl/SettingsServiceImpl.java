package nl.rikp.customerService.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.dto.SettingsRequest;
import nl.rikp.customerService.dto.SettingsResponse;
import nl.rikp.customerService.exception.notFound.SettingsNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.mapper.SettingsMapper;
import nl.rikp.customerService.model.Language;
import nl.rikp.customerService.model.PreferredMetric;
import nl.rikp.customerService.model.Settings;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.repository.SettingsRepository;
import nl.rikp.customerService.service.SettingsService;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class SettingsServiceImpl extends CustomerBaseService implements SettingsService {
    private final SettingsRepository repo;
    private final SettingsMapper mapper;

    public SettingsServiceImpl(
            CustomerRepository customerRepo,
            CustomerMapper customerMapper,
            SettingsRepository settingsRepository,
            SettingsMapper mapper
    ) {
        super(customerRepo, customerMapper);
        this.repo = settingsRepository;
        this.mapper = mapper;
    }

    public SettingsResponse getSettingsByCustomerId(Long customerId) {
        return mapper.fromSettings(fetchSettings(customerId));
    }

    @Transactional
    public SettingsResponse createSettingsForCustomer(Long customerId, SettingsRequest request) {
        log.info("Creating settings with data: {}", request);
        var customer = getCustomer(customerId);

        var settings = repo.save( mapper.toSettings(customer, request));

        log.info("Settings created successfully.");
        return mapper.fromSettings(settings);
    }

    public SettingsResponse updateDarkMode(Long customerId, boolean useDarkMode) {
        log.info("Updating dark mode setting for customer ID {}.", customerId);

        Settings settings = fetchSettings(customerId);
        settings.updateDarkMode(useDarkMode);

        log.info("Dark mode setting updated successfully.");
        return mapper.fromSettings(settings);
    }

    public SettingsResponse updatePreferredMetrics(Long customerId, PreferredMetric preferredMetric) {
        log.info("Updating preferred metric for customer ID {}.", customerId);

        Settings settings = fetchSettings(customerId);
        settings.updatePreferredMetric(preferredMetric);

        log.info("Preferred metric updated successfully.");
        return mapper.fromSettings(settings);
    }

    public SettingsResponse updateNotifications(Long customerId, boolean notificationsEnabled) {
        log.info("Updating notifications setting for customer ID {}.", customerId);

        Settings settings = fetchSettings(customerId);
        settings.updateNotificationsEnabled(notificationsEnabled);

        log.info("Notifications setting updated successfully.");
        return mapper.fromSettings(settings);
    }

    public SettingsResponse updateLanguage(Long customerId, Language language) {
        log.info("Updating language setting for customer ID {}.", customerId);

        Settings settings = fetchSettings(customerId);
        settings.updateLanguage(language);

        log.info("Language setting updated successfully.");
        return mapper.fromSettings(settings);
    }

    public SettingsResponse updateItemsPerPage(Long customerId, int itemsPerPage) {
        log.info("Updating items per page setting for customer ID {}.", customerId);

        Settings settings = fetchSettings(customerId);
        settings.updateItemsPerPage(itemsPerPage);

        log.info("Items per page setting updated successfully.");
        return mapper.fromSettings(settings);
    }

    protected Settings fetchSettings(Long customerId) {
        return repo.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    log.error("No settings found for customer ID {}.", customerId);
                    return new SettingsNotFoundException(
                            format("No settings found for customer ID:: %s", customerId)
                    );
                });
    }

    public void removeSettingsFromCustomer(Long customerId) {
        log.info("Removing settings for customer ID {}.", customerId);
        var settings = fetchSettings(customerId);

        repo.delete(settings);
        log.info("settings removed successfully.");
    }
}
