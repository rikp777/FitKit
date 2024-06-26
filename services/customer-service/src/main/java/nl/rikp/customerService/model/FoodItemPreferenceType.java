package nl.rikp.customerService.model;

import lombok.Getter;

/**
 * Enum representing the preference types for food items.
 *
 * Each preference type is associated with a description and flags indicating if it is preferred or disliked.
 */
public enum FoodItemPreferenceType {
    PREFERRED("preferences", true, false),
    DISLIKED("dislikes", false, true),
    NEUTRAL("neutral", false, false);

    @Getter
    private final String description;
    private final boolean isPreferred;
    private final boolean isDisliked;

    FoodItemPreferenceType(String description, boolean isPreferred, boolean isDisliked) {
        this.description = description;
        this.isPreferred = isPreferred;
        this.isDisliked = isDisliked;
    }

    /**
     * Checks if the preference type is preferred.
     *
     * @return true if preferred, false otherwise
     */
    public boolean isPreferred() {
        return isPreferred;
    }

    /**
     * Checks if the preference type is disliked.
     *
     * @return true if disliked, false otherwise
     */
    public boolean isDisliked() {
        return isDisliked;
    }

    /**
     * Checks if the preference type is neutral.
     *
     * @return true if neutral, false otherwise
     */
    public boolean isNeutral() {
        return !isPreferred && !isDisliked;
    }

    /**
     * Converts a string representation of a preference type description to the corresponding FoodItemPreferenceType enum.
     *
     * @param description the description of the preference type
     * @return the matching FoodItemPreferenceType
     * @throws IllegalArgumentException if the description does not match any preference type
     */
    public static FoodItemPreferenceType fromString(String description) {
        for (FoodItemPreferenceType type : FoodItemPreferenceType.values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                String.format("Invalid preference type description: '%s'. Supported descriptions are: %s",
                        description,
                        String.join(", ", getSupportedDescriptions()))
        );
    }

    /**
     * Gets the supported preference type descriptions.
     *
     * @return an array of supported preference type descriptions
     */
    private static String[] getSupportedDescriptions() {
        return new String[] {
                PREFERRED.description, DISLIKED.description, NEUTRAL.description
        };
    }
}