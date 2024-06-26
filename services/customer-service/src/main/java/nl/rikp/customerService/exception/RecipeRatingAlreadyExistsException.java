package nl.rikp.customerService.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeRatingAlreadyExistsException extends RuntimeException {
    private final String message;

    public RecipeRatingAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

    public RecipeRatingAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}