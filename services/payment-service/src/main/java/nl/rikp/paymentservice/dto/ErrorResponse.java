package nl.rikp.paymentservice.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        OffsetDateTime timestamp,
        String message,
        Map<String, String> validationErrors
) {

    public ErrorResponse(String code, String message) {
        this(code, OffsetDateTime.now(), message, null);
    }


    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", validationErrors=" + validationErrors +
                '}';
    }
}
