package nl.rikp.customerService.model;

import lombok.Getter;

/**
 * Enum representing the rating types for a recipe.
 * Each rating type includes a numeric value and a description.
 */
@Getter
public enum RecipeRatingType {
    ONE_STAR(1, "Poor"),
    TWO_STAR(2, "Fair"),
    THREE_STAR(3, "Good"),
    FOUR_STAR(4, "Very Good"),
    FIVE_STAR(5, "Excellent");

    private final int rating;
    private final String description;

    RecipeRatingType(int rating, String description) {
        this.rating = rating;
        this.description = description;
    }

    /**
     * Returns the RecipeRatingType corresponding to the given value.
     *
     * @param value the numeric value of the rating
     * @return the matching RecipeRatingType
     * @throws IllegalArgumentException if the value is not between 1 and 5
     */
    public static RecipeRatingType fromValue(int value) {
        for (RecipeRatingType rating : values()) {
            if (rating.rating == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating: " + value + ". Valid values are between 1-5.");
    }

    /**
     * Returns a string representation of the rating type.
     *
     * @return a string combining the rating value and description
     */
    @Override
    public String toString() {
        return rating + " - " + description;
    }
}