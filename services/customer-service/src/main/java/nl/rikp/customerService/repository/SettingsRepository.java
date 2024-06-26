package nl.rikp.customerService.repository;

import nl.rikp.customerService.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findByCustomerId(Long customerId);
}
