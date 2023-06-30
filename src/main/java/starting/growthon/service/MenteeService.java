package starting.growthon.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.MentoringDto;
import starting.growthon.dto.ScheduleDto;
import starting.growthon.dto.UrlDto;
import starting.growthon.dto.response.MentoringResponseDto;
import starting.growthon.entity.User;
import starting.growthon.repository.MentorInfoRepository;
import starting.growthon.repository.UserRepository;
import starting.growthon.util.UserUtil;

import java.util.List;

@Service
@Transactional
public class MenteeService {
    private final UserUtil userUtil;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private MimeMessageHelper mimeMessageHelper;
    private final MentorInfoRepository mentorInfoRepository;
    public MenteeService(UserUtil userUtil, UserRepository userRepository, JavaMailSender javaMailSender,
                         MentorInfoRepository mentorInfoRepository) {
        this.userUtil = userUtil;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.mentorInfoRepository = mentorInfoRepository;
    }


    public MentoringResponseDto mentoring(MentoringDto mentoringDto, Long uuid) throws MessagingException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        User mentee = userUtil.getLoggedInUser();
        User mentor = userRepository.findByUuid(uuid).filter(user -> user.getRole().equals("MENTOR")).get();
        MentoringResponseDto response = new MentoringResponseDto();

        response.setUrl(mentoringDto.getUrl());
        response.setPhone(mentoringDto.getPhone());
        response.setContent(mentoringDto.getContent());
        response.setEmail(mentoringDto.getEmail());
        response.setSchedules(mentoringDto.getSchedules());

        // simpleMailMessage.setTo(mentor.getEmail());
        // simpleMailMessage.setSubject("🎉 멘토링 요청이 들어왔습니다. 🎉");
        mimeMessageHelper.setTo(mentor.getEmail());
        mimeMessageHelper.setSubject("🎉 멘토링 요청이 들어왔습니다. 🎉");

        String emailContent =
                "<img src=\"https://growthonbucket.s3.ap-northeast-2.amazonaws.com/banner.png\"></img>" +
                "<h2>멘티 이메일</h2>" + mentee.getEmail() + "\n" +
                "<h2>멘티 전화번호</h2>" + mentoringDto.getPhone() + "\n" +
                "<h2>멘티 이력서 url</h2>" + convertUrlsToString(mentoringDto.getUrl()) + "\n" +
                "<h2>멘티 이용 가능 스케줄</h2>" + convertSchedulesToString(mentoringDto.getSchedules()) + "\n" +
                "<h2>멘티 요청 내용</h2>" + mentoringDto.getContent();

        response.setEmailContent(emailContent);

        // simpleMailMessage.setText(emailContent);
        mimeMessageHelper.setText(emailContent, true);
        javaMailSender.send(message);

        int count = mentorInfoRepository.findByMentorId(mentor.getId()).getCount();
        mentorInfoRepository.findByMentorId(mentor.getId()).setCount(count + 1);
        return response;
    }

    private String convertSchedulesToString(List<ScheduleDto> schedules) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (ScheduleDto schedule : schedules) {
            sb.append("<li>")
                    .append(schedule.getDay()).append(": ");
            for (int i = 0; i < schedule.getTime().size(); i++) {
                sb.append(schedule.getTime().get(i));
                if (i < schedule.getTime().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private String convertUrlsToString(List<UrlDto> urls) {
        StringBuilder sb = new StringBuilder();
        for (UrlDto url : urls) {
            sb.append("<a href=\"").append(url.getLink()).append("\">")
                    .append(url.getCaption()).append("</a>").append("\n");
        }
        return sb.toString();
    }
}
