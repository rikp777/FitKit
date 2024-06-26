package nl.rikp.customerService.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FoodItemAlreadyExistsException extends RuntimeException {
    private final String message;

    public FoodItemAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

    public FoodItemAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}