package nl.rikp.customerService.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StreakEmptyException extends RuntimeException {
    private final String message;

    public StreakEmptyException(String message) {
        super(message);
        this.message = message;
    }

    public StreakEmptyException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}