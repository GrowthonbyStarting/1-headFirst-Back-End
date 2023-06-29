package starting.growthon.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.MentoringDto;
import starting.growthon.dto.ScheduleDto;
import starting.growthon.dto.response.MentoringResponseDto;
import starting.growthon.entity.User;
import starting.growthon.repository.UserRepository;
import starting.growthon.util.UserUtil;

import java.util.List;

@Service
@Transactional
public class MenteeService {
    private final UserUtil userUtil;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    public MenteeService(UserUtil userUtil, UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userUtil = userUtil;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }


    public MentoringResponseDto mentoring(MentoringDto mentoringDto, Long uuid) {
        System.out.println(mentoringDto);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        User mentee = userUtil.getLoggedInUser();
        User mentor = userRepository.findByUuid(uuid).filter(user -> user.getRole().equals("MENTOR")).get();
        MentoringResponseDto response = new MentoringResponseDto();

        response.setUrl(mentoringDto.getUrl());
        response.setPhone(mentoringDto.getPhone());
        response.setContent(mentoringDto.getContent());
        response.setEmail(mentoringDto.getEmail());
        response.setSchedules(mentoringDto.getSchedules());

        simpleMailMessage.setTo(mentor.getEmail());
        simpleMailMessage.setSubject("🎉 멘토링 요청이 들어왔습니다. 🎉");

        String emailContent =
                "멘티 이메일: " + mentee.getEmail() + "\n" +
                "멘티 전화번호: " + mentoringDto.getPhone() + "\n" +
                "멘티 이력서 url: " + mentoringDto.getUrl() + "\n" +
                "멘티 이용 가능 스케줄: " + convertSchedulesToString(mentoringDto.getSchedules()) + "\n" +
                "멘티 요청 내용: " + mentoringDto.getContent();

        response.setEmailContent(emailContent);

        simpleMailMessage.setText(emailContent);

        javaMailSender.send(simpleMailMessage);

        return response;
    }

    private String convertSchedulesToString(List<ScheduleDto> schedules) {
        StringBuilder sb = new StringBuilder();
        for (ScheduleDto schedule : schedules) {
            sb.append(schedule.toEmailString());
        }
        return sb.toString();
    }
}
