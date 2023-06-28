package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MentorAndSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "mentor_info_id")
    @JsonIgnore
    private MentorInfo mentor;

    public MentorAndSchedule(Schedule schedule, MentorInfo mentor) {
        this.schedule = schedule;
        this.mentor = mentor;
    }
}
