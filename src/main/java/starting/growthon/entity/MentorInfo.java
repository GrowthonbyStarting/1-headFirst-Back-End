package starting.growthon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MentorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_info_id")
    @JsonIgnore
    private Long id;

    private String title; // 멘토링 제목

    private String introduce; // 멘토 소개

    private String possibles; // 멘토링 가능 분야

    private String concept; // 멘토링 주제

    private String target; // 멘토링 대상

    private String prepare; // 멘티 준비사항

    private String curriculum; // 진행 방식

    private String rule; // 진행 수단
    private String time; // 1회 진행 시간

    // 멘토링 진행 시간에 대한 내용도 담아야 함
    private int cost;

    private int view;

    private boolean verified;

    private int count; // 피드백 횟수 (임시)
    
    @OneToOne
    private User mentor;

    public MentorInfo(String introduce, int cost, int count, int view, User mentor) {
        this.introduce = introduce;
        this.cost = cost;
        this.count = count;
        this.view = view;
        this.mentor = mentor;
    }
}
