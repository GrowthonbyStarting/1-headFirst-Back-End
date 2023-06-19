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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String email;
    private String name;
    private Long uuid; // 카카오 고유 ID 위함

    private String role;

    @OneToOne
    @JoinColumn(name = "mentor_info_id")
    private MentorInfo mentorInfo;

    public User(String name, Long uuid) {
        this.name = name;
        this.uuid = uuid;
        this.role = "MENTEE";
    }
}