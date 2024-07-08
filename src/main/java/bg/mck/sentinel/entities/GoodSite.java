package bg.mck.sentinel.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "good_sites")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class GoodSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String domain;
}
