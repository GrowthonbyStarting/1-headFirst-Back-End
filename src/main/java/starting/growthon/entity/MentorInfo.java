package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MentorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String content;

    private int cost;

    private int view;

    @OneToOne
    private User mentor;

    public MentorInfo(String content, int cost, int view, User mentor) {
        this.content = content;
        this.cost = cost;
        this.view = view;
        this.mentor = mentor;
    }
}
