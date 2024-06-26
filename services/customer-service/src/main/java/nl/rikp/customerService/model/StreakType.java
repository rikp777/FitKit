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
}
