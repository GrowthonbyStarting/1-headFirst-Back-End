package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    @JsonIgnore
    private Long id;

    private String day; // 요일

    private String time; // 시각 (일단 문자열)

    public Schedule(String day, String time) {
        this.day = day;
        this.time = time;
    }
}
