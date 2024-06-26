package nl.rikp.paymentservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
        Long id,
        @NotNull(message = "First name is required")
        String firstName,
        @NotNull(message = "Last name is required")
        String lastName,
        @NotNull(message = "Email is required")
        @Email(message = "Email is not valid")
        String email,
        @NotNull(message = "Phone number is required")
        String phone
) {
}
