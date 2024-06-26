package nl.rikp.customerService.exception.notFound;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeRatingNotFoundException extends RuntimeException {
    private final String message;

    public RecipeRatingNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public RecipeRatingNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
