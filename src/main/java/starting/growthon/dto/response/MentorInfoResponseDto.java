package starting.growthon.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import starting.growthon.dto.ScheduleDto;
import starting.growthon.entity.User;

import java.util.List;

@Getter
@Setter
@Builder
public class MentorInfoResponseDto {

    private User mentor;
    private String title; // 멘토링 제목
    private String introduce; // 멘토 소개
    private String possibles; // 멘토링 가능 분야
    private String concept; // 멘토링 주제
    private String target; // 멘토링 대상
    private String prepare; // 멘티 준비사항
    private String curriculum; // 진행 방식
    private String rule; // 진행 수단
    private String time; // 1회 진행 시간
    private int cost; // 1회 진행 가격
    private int view;
    private int count;
    private int followers;
    private boolean verified;
    private String profile;

    // 멘토링 진행 시간에 대한 내용도 담아야 함
    private List<ScheduleDto> schedules;
}
