package bg.mck.sentinel.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "penalized_users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PenalizedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Integer timesHasBeenPunished;
    private String asMentioned;
    private String punishedBy;
    private String reason;
    private LocalDateTime executionTime;
}
