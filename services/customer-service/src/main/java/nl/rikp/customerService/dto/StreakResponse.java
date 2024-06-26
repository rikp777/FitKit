package nl.rikp.customerService.dto;

import nl.rikp.customerService.model.Streak;

public record StreakResponse(
        String name,
        String count
) {

    public static StreakResponse fromStreak(Streak streak) {
        return new StreakResponse(
                streak.getType().name(),
                String.valueOf(streak.getCurrentStreakCount())
        );
    }
}
