package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.StreakResponse;
import nl.rikp.customerService.model.StreakType;

import java.util.List;

public interface StreakService {
    List<StreakResponse> getAllStreaksByCustomerId(Long customerId);
    StreakResponse getStreaksByCustomerIdAndType(Long customerId, StreakType streakType);
    void buyStreaksForCustomer(Long customerId, int count);
}
