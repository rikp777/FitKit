package nl.rikp.customerService.model;

import lombok.Getter;

/**
 * Enum representing different premium levels for a customer.
 *
 * Each premium level is associated with a numeric value indicating its rank.
 */
@Getter
public enum PremiumLevel {
    BASIC(1),
    SILVER(2),
    GOLD(3),
    PLATINUM(4);

    private final int level;

    PremiumLevel(int level) {
        this.level = level;
    }

    /**
     * Converts a string representation of a premium level to the corresponding PremiumLevel enum.
     *
     * @param levelName the name of the premium level
     * @return the matching PremiumLevel
     * @throws IllegalArgumentException if the levelName does not match any premium level
     */
    public static PremiumLevel fromString(String levelName) {
        for (PremiumLevel level : PremiumLevel.values()) {
            if (level.name().equalsIgnoreCase(levelName)) {
                return level;
            }
        }
        throw new IllegalArgumentException(
                String.format("Invalid premium level: '%s'. Supported levels are: %s",
                        levelName,
                        String.join(", ", getSupportedLevelNames()))
        );
    }

    /**
     * Gets the supported premium level names.
     *
     * @return an array of supported premium level names
     */
    private static String[] getSupportedLevelNames() {
        return new String[] {
                BASIC.name(), SILVER.name(), GOLD.name(), PLATINUM.name()
        };
    }
}
