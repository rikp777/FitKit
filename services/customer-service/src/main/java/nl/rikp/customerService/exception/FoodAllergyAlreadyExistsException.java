package nl.rikp.customerService.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FoodAllergyAlreadyExistsException extends RuntimeException {
    private final String message;

    public FoodAllergyAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

    public FoodAllergyAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}