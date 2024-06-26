package nl.rikp.customerService.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PremiumFeatureException extends RuntimeException {
    private final String message;

    public PremiumFeatureException(String message) {
        super(message);
        this.message = message;
    }

    public PremiumFeatureException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
