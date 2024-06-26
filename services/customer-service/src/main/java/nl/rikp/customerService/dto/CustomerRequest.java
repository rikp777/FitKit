package nl.rikp.customerService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import nl.rikp.customerService.model.Language;

@Builder
public record CustomerRequest (
        Long id,
        @NotNull(message = "Customer firstname is required")
        String firstName,
        @NotNull(message = "Customer lastname is required")
        String lastName,
        @NotNull(message = "Customer email is required")
        @Email(message = "Customer email is not a valid email address")
        String email,
        String phone,
        String language
){

}
