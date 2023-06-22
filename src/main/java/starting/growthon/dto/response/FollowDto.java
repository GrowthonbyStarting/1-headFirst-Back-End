package starting.growthon.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto {

    private Long follower;
    private Long mentor;

    public FollowDto(Long follower, Long mentor) {
        this.follower = follower;
        this.mentor = mentor;
    }
}
