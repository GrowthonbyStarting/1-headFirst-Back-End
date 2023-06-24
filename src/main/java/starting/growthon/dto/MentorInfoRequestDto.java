package starting.growthon.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MentorInfoRequestDto {
    private String name;
    private String nickname;
    private String company;
    private String job;
    private String subjob;
    private String year;
    private String title;
    private String content;
    private List<String> keywords;
}
