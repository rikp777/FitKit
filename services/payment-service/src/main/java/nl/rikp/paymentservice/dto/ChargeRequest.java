package nl.rikp.paymentservice.dto;

public record ChargeRequest(
        String token,
        Long amount
) {
}
