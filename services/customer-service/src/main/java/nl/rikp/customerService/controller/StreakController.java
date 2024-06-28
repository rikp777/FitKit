package nl.rikp.customerService.controller;

import lombok.RequiredArgsConstructor;
import nl.rikp.customerService.dto.StreakResponse;
import nl.rikp.customerService.model.StreakType;
import nl.rikp.customerService.service.StreakService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customer-id}/streak")
@Validated
@RequiredArgsConstructor
public class StreakController {

    private final StreakService streakService;

    @GetMapping
    public ResponseEntity<List<StreakResponse>> getAllStreaks(
            @PathVariable("customer-id") Long customerId
    ) {
        List<StreakResponse> streaks = streakService.getAllStreaksByCustomerId(customerId);
        return ResponseEntity.ok(streaks);
    }

    @GetMapping("/type/{streak-type}")
    public ResponseEntity<StreakResponse> getStreaksByType(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("streak-type") String streakTypeValue) {
        StreakType streakType = StreakType.valueOf(streakTypeValue);
        StreakResponse streaks = streakService.getStreaksByCustomerIdAndType(customerId, streakType);
        return ResponseEntity.ok(streaks);
    }

    @PostMapping("/buy/{amount}")
    public ResponseEntity<String> buyStreaks(
            @PathVariable("customer-id") Long customerId,
            @PathVariable("amount") int amount
    ) {
        streakService.buyStreaksForCustomer(customerId, amount);
        return ResponseEntity.ok("Streaks bought successfully");
    }
}
