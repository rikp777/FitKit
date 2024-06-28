package nl.rikp.customerService.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.dto.StreakResponse;
import nl.rikp.customerService.exception.StreakEmptyException;
import nl.rikp.customerService.exception.notFound.CustomerNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.mapper.StreakMapper;
import nl.rikp.customerService.model.CoinTransaction;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.PremiumLevel;
import nl.rikp.customerService.model.StreakType;
import nl.rikp.customerService.repository.CoinTransactionRepository;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.repository.StreakFreezeRepository;
import nl.rikp.customerService.repository.StreakRepository;
import nl.rikp.customerService.service.StreakService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Service
@Slf4j
public class StreakServiceImpl extends CustomerBaseService implements StreakService {
    private static final int COST_PER_STREAK = 200;
    private static final int MAX_STREAKS_STANDARD = 2;
    private static final int MAX_STREAKS_PREMIUM = 3;

    private final StreakRepository streakRepository;
    private final StreakFreezeRepository streakFreezeRepository;
    private final CoinTransactionRepository coinTransactionRepository;
    private final StreakMapper streakMapper;

    public StreakServiceImpl(
            CustomerRepository customerRepo,
            CustomerMapper customerMapper,
            StreakRepository streakRepository,
            StreakFreezeRepository streakFreezeRepository,
            CoinTransactionRepository coinTransactionRepository,
            StreakMapper streakMapper
    ) {
        super(customerRepo, customerMapper);
        this.streakRepository = streakRepository;
        this.streakFreezeRepository = streakFreezeRepository;
        this.coinTransactionRepository = coinTransactionRepository;
        this.streakMapper = streakMapper;
    }

    @Override
    public List<StreakResponse> getAllStreaksByCustomerId(Long customerId) {
        return streakRepository.findActiveStreaksByCustomerId(customerId).stream()
                .map(streakMapper::fromStreak)
                .toList();
    }

    @Override
    public StreakResponse getStreaksByCustomerIdAndType(Long customerId, StreakType streakType) {
        return streakRepository.getStreaksByCustomerIdAndType(customerId, streakType)
                .map(streakMapper::fromStreak)
                .orElseThrow(() -> new StreakEmptyException(
                        format("Streak empty for provided Streak type:: %s", streakType.getDescription())
                ));
    }

    @Override
    public void buyStreaksForCustomer(Long customerId, int count) {
        Customer customer = getCustomer(customerId);

        // Calculate the max streaks allowed based on premium level
        int maxStreaksAllowed = customer.getPremiumLevel() != null ? MAX_STREAKS_PREMIUM : MAX_STREAKS_STANDARD;

        // Check the current number of streak freezes the customer has
        int currentStreaks = streakFreezeRepository.countNonActivatedStreakFreezesByCustomerId(customerId);

        // Calculate the new total number of streaks if the purchase is allowed
        int newTotalStreaks = currentStreaks + count;
        if (newTotalStreaks > maxStreaksAllowed) {
            throw new IllegalArgumentException("Cannot buy more streaks than allowed. Max allowed: " + maxStreaksAllowed);
        }

        int totalCost = count * COST_PER_STREAK;
        if (customer.getCoins() < totalCost) {
            throw new IllegalArgumentException("Insufficient coins");
        }
        int balanceBefore = customer.getCoins();

        customer.subtractCoins(totalCost);
        customerRepo.save(customer);

        streakFreezeRepository.addFreezesToCustomer(customerId, count);

        CoinTransaction expenditureTransaction = CoinTransaction.builder()
                .customer(customer)
                .transactionType(CoinTransaction.TransactionType.EXPENDITURE)
                .amount(totalCost)
                .transactionDate(LocalDateTime.now())
                .balanceBefore(balanceBefore)
                .build();

        coinTransactionRepository.save(expenditureTransaction);
    }
}
