package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MentorAndBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    @JsonIgnore
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badge badge;

    public MentorAndBadge(User mentor, Badge badge) {
        this.mentor = mentor;
        this.badge = badge;
    }
}
