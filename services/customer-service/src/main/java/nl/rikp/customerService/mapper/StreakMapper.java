package nl.rikp.customerService.mapper;

import nl.rikp.customerService.dto.StreakResponse;
import nl.rikp.customerService.model.Streak;
import org.springframework.stereotype.Component;


@Component
public class StreakMapper {

    public StreakResponse fromStreak(Streak streak) {
        return new StreakResponse(
                streak.getType().name(),
                String.valueOf(streak.getCurrentStreakCount())
        );
    }

//    public Streak toStreak(Customer customer, StreakRequest request) {
//        return Streak.builder()
//                .customer(customer)
//                .type(StreakType.fromString(request.type()))
//                .currentStreakCount(request.currentStreakCount())
//                .build();
//    }
}
