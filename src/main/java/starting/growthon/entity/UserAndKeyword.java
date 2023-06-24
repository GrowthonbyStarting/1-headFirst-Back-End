package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class UserAndKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Keyword keyword;

    public UserAndKeyword(User user, Keyword keyword) {
        this.user = user;
        this.keyword = keyword;
    }
}
