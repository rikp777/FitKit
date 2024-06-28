package nl.rikp.customerService.model;

public enum StreakType {
    WORKOUT("Workout"),
    DIET("Diet"),
    HYDRATION("Hydration"),
    SLEEP("Sleep"),
    MEDITATION("Meditation");

    private final String description;

    StreakType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    public static StreakType fromString(String description) {
        for (StreakType type : StreakType.values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown StreakType: " + description);
    }
}
