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

    private String title;

    private String content;

    private int cost;

    private int view;

    private boolean verified;

    private int count; // 피드백 횟수 (임시)
    
    @OneToOne
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "univ_id")
    private Univ univ;

    public MentorInfo(String content, int cost, int view, User mentor, Univ univ) {
        this.content = content;
        this.cost = cost;
        this.view = view;
        this.mentor = mentor;
        this.univ = univ;
    }
}
