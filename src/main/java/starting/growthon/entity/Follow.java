package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne
    private User mentor;

    @ManyToOne
    private User mentee;

    public Follow(User mentor, User mentee) {
        this.mentor = mentor;
        this.mentee = mentee;
    }
}
