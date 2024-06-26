package nl.rikp.notificationService.model;

public record Customer(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
