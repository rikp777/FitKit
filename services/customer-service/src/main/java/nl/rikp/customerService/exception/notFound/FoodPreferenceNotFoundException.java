package nl.rikp.customerService.exception.notFound;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class FoodPreferenceNotFoundException extends RuntimeException {
    private final String message;

    public FoodPreferenceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public FoodPreferenceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
