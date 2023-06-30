package starting.growthon.dto.response;

import lombok.Getter;
import lombok.Setter;
import starting.growthon.dto.ScheduleDto;
import starting.growthon.dto.UrlDto;

import java.util.List;

@Getter
@Setter
public class MentoringResponseDto {
    private List<ScheduleDto> schedules;
    private String phone; // 전화번호
    private String email; // 이메일
    private List<UrlDto> url; // 이력서 url
    private String content; // 멘토링 받고 싶은 내용
    private String emailContent; // 이메일 내용
}
