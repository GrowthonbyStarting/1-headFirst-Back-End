package starting.growthon.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import starting.growthon.entity.User;

@Getter
@Setter
@Builder
public class MentorInfoDto {

    private User mentor;
    private String content;
    private int cost;
    private int view;
    private int followers;
    private boolean summary;
    private String univ;
    private String profile;
}
