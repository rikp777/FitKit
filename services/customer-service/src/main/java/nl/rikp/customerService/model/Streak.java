package nl.rikp.customerService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a user streak, which tracks the start and end dates of a streak,
 * the type of streak, and the current streak count.
 */
//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(
        name = "streaks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_customer_id_streak_type",
                        columnNames = {"customer_id", "type"}
                )
        },
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id"),
                @Index(name = "idx_customer_streak", columnList = "customer_id, type")
        }
)
public class Streak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private StreakType type;

    /**
     * Calculates the current streak count based on the start date and end date.
     * If the end date is null, the current date is used as the end date.
     *
     * @return the current streak count
     */
    @Transient
    public int getCurrentStreakCount() {
        LocalDate start = convertToLocalDateViaInstant(startDate);
        LocalDate end = endDate != null ? convertToLocalDateViaInstant(endDate) : LocalDate.now();
        return (int) ChronoUnit.DAYS.between(start, end) + 1; // +1 to include both start and end dates
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public void endStreak() {
        this.endDate = Date.valueOf(LocalDate.now());
    }
}
