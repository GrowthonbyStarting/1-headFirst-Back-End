package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MentorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private Long cost;

    private String content;

    private Boolean summary;

    @JsonIgnore
    @OneToOne(mappedBy = "mentorInfo")
    private User mentor;

    public MentorInfo(Long cost, String content, Boolean summary, User mentor) {
        this.cost = cost;
        this.content = content;
        this.summary = summary;
        this.mentor = mentor;
    }
}
