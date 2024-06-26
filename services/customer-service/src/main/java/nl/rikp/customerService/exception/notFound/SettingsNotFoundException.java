package nl.rikp.customerService.exception.notFound;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SettingsNotFoundException extends RuntimeException {
    private final String message;

    public SettingsNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public SettingsNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
