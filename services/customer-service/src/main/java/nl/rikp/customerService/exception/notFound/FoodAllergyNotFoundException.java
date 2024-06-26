package nl.rikp.customerService.exception.notFound;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FoodAllergyNotFoundException extends RuntimeException {
    private final String message;

    public FoodAllergyNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public FoodAllergyNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}