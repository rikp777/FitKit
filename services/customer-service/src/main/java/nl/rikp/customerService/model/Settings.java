package nl.rikp.customerService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents the settings for a customer.
 *
 * This entity holds various user preferences including dark mode usage, preferred metric system,
 * notifications enabled status, language, and items per page. It has a one-to-one relationship with
 * the Customer entity.
 *
 * The cascade type for the customer relationship is set to ALL to ensure that any changes to the
 * Customer entity are reflected in the associated Settings entity.
 */
//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(
        name = "settings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="unique_customer_id",
                        columnNames = {"customer_id"}
                )
        },
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id"),
        }
)
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer must not be null")
    private Customer customer;

    @Column(name = "use_dark_mode", nullable = false)
    @NotNull(message = "Dark mode preference must not be null")
    private boolean useDarkMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_metrics", nullable = false)
    @NotNull(message = "Preferred metric cannot be null")
    private PreferredMetric preferredMetric;

    @Column(name = "notifications_enabled", nullable = false)
    @NotNull(message = "Notifications enabled preference must not be null")
    private boolean notificationsEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    @NotNull(message = "Language cannot be null")
    private Language language;

    @Column(name = "items_per_page", nullable = false)
    @Min(value = 1, message = "Items per page must be at least 1")
    @Max(value = 100, message = "Items per page must not be more than 100")
    private int itemsPerPage;

    /**
     * Updates the dark mode setting.
     *
     * @param useDarkMode true to enable dark mode, false to disable
     */
    public void updateDarkMode(boolean useDarkMode){
        this.useDarkMode = useDarkMode;
    }

    /**
     * Updates the preferred metric system.
     *
     * @param preferredMetric the new preferred metric
     */
    public void updatePreferredMetric(PreferredMetric preferredMetric){
        this.preferredMetric = preferredMetric;
    }

    /**
     * Updates the notifications enabled setting.
     *
     * @param notificationsEnabled true to enable notifications, false to disable
     */
    public void updateNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    /**
     * Updates the language setting.
     *
     * @param language the new language
     */
    public void updateLanguage(Language language) {
        this.language = language;
    }

    /**
     * Updates the number of items per page.
     *
     * @param itemsPerPage the new number of items per page
     */
    public void updateItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}
