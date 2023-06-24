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

    @ManyToOne
    @JoinColumn(name = "year_id")
    private Year year;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "sub_job_id")
    private SubJob subjob;

    public User(Long uuid, String email) {
        this.uuid = uuid;
        this.email = email;
        this.role = "MENTEE";
    }
}