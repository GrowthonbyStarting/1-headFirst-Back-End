package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private Long uuid;

    private String name;

    private String nickname;

    private String email;

    private String role;

    private int year;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "sub_job_id")
    private SubJob subjob;

    public User(Long uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.role = "MENTEE";
    }
}