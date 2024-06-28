package nl.rikp.customerService.job;

import lombok.RequiredArgsConstructor;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.Streak;
import nl.rikp.customerService.repository.StreakFreezeRepository;
import nl.rikp.customerService.repository.StreakRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;



//todo base the streak end on customer activity
//todo run the cron job every hour to check if the streak has ended for timezone
@Service
@RequiredArgsConstructor
public class StreakCronJob {
    private static final Random RANDOM = new Random();
    private static final int BATCH_SIZE = 100;

    private final StreakRepository userStreakRepository;
    private final StreakFreezeRepository streakFreezeRepository;

    @Scheduled(cron = "0 0 * * * ?") // Every hour
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
                Customer customer = streak.getCustomer();

                //todo check if user has been active
                //check activity
                boolean isCustomerInactive = RANDOM.nextBoolean(); //customerActivityService.isCustomerActive(customer.getId());

                // if no activity end streak
                boolean hasFreezes  = !streakFreezeRepository.hasAvailableFreezes(customer.getId());

                if (isCustomerInactive  && !hasFreezes) {
                    // if no streak freezes and no activity end streak
                    streak.endStreak();
                    userStreakRepository.save(streak);
                }else if(hasFreezes){
                    // use available freeze
                    streakFreezeRepository.useStreakFreezeForCustomer(customer.getId());
                }
            }
        });
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
}
