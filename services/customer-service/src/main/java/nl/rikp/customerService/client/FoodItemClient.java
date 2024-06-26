package nl.rikp.customerService.client;

import org.springframework.stereotype.Service;

@Service
public class FoodItemClient {
    public boolean existsById(Long foodItemId) {
        return true;
    }
}
