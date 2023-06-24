package starting.growthon.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import starting.growthon.entity.User;

import java.util.List;

@Getter
@Setter
@Builder
public class MentorInfoResponseDto {

    private User mentor;
    private String content;
    private int cost;
    private int view;
    private int followers;
    private boolean verified;
    private String univ;
    private String profile;
    private List<String> keywords;
}
