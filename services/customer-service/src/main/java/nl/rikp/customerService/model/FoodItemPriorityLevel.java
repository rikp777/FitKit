package nl.rikp.customerService.model;

import lombok.Getter;

/**
 * Enum representing the priority levels for food items.
 *
 * Each priority level is associated with a numeric value.
 */
@Getter
public enum FoodItemPriorityLevel {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int value;

    FoodItemPriorityLevel(int value) {
        this.value = value;
    }

    /**
     * Converts a numeric value to the corresponding FoodItemPriorityLevel enum.
     *
     * @param value the numeric value of the priority level
     * @return the matching FoodItemPriorityLevel
     * @throws IllegalArgumentException if the value is not between the minimum and maximum values
     */
    public static FoodItemPriorityLevel fromValue(int value) {
        for (FoodItemPriorityLevel level : values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException(
                String.format("Invalid priority level: %d. You can choose a value between %d and %d.",
                        value,
                        getMinValue(),
                        getMaxValue())
        );
    }

    /**
     * Gets the minimum priority level value.
     *
     * @return the minimum value
     */
    public static int getMinValue() {
        return ONE.value;
    }

    /**
     * Gets the maximum priority level value.
     *
     * @return the maximum value
     */
    public static int getMaxValue() {
        return FIVE.value;
    }
}
