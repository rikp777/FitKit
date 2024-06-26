package nl.rikp.customerService.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FoodPreferenceRemoveException extends RuntimeException {
    private final String message;

    public FoodPreferenceRemoveException(String message) {
        super(message);
        this.message = message;
    }

    public FoodPreferenceRemoveException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
