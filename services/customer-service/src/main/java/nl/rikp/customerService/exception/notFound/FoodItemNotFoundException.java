package nl.rikp.customerService.exception.notFound;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FoodItemNotFoundException extends RuntimeException {
    private final String message;

    public FoodItemNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public FoodItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}