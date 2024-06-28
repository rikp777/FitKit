package nl.rikp.customerService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a freeze applied to a streak, which allows users to pause their streak without losing progress.
 */
//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(name = "streak_freezes")
public class StreakFreeze {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "date_received", nullable = false)
    private LocalDate dateReceived;

    @Column(name = "date_used")
    private LocalDate dateUsed;

    public void use(){
        this.dateUsed = LocalDate.now();
    }
}
