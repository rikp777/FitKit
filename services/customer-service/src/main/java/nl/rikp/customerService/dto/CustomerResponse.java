package nl.rikp.customerService.dto;

import lombok.Builder;
import nl.rikp.customerService.model.Settings;

import java.util.List;

@Builder
public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        FoodPreferenceGroupsResponse foodPreferenceGroups,
        List<FoodAllergyResponse> foodAllergies,
        SettingsResponse settings,
        List<StreakResponse> streaks
) {
}
