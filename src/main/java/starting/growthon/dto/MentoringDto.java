package starting.growthon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MentoringDto {
    private List<ScheduleDto> schedules;
    private String phone; // 전화번호
    private String email; // 이메일
    private List<String> url; // 이력서 url
    private String content; // 멘토링 받고 싶은 내용
}
