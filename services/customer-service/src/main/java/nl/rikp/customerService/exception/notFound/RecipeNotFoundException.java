package nl.rikp.customerService.exception.notFound;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeNotFoundException extends RuntimeException {
    private final String message;

    public RecipeNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public RecipeNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}