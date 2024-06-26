package nl.rikp.customerService.job;

import lombok.RequiredArgsConstructor;
import nl.rikp.customerService.model.Streak;
import nl.rikp.customerService.repository.StreakRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


//todo base the streak end on customer activity
@Service
@RequiredArgsConstructor
public class StreakCronJob {
    private static final int BATCH_SIZE = 100;

    private final StreakRepository userStreakRepository;

    @Scheduled(cron = "0 0 12 * * ?") // Every day at 12 PM
    public void updateStreaks() {
        int pageNumber = 0;
        List<Streak> streakBatch;

        do {
            streakBatch = userStreakRepository.findActiveStreaksInBatches(pageNumber, BATCH_SIZE);
            processStreakBatch(streakBatch);
            pageNumber++;
        } while (!streakBatch.isEmpty());
    }

    private void processStreakBatch(List<Streak> streakBatch) {
        streakBatch.parallelStream().forEach(streak -> {
            LocalDate lastEndDate = convertToLocalDateViaInstant(streak.getEndDate());
            LocalDate today = LocalDate.now();

            if (!lastEndDate.equals(today)) {
                streak.endStreak();
                userStreakRepository.save(streak);
            }
        });
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
}
