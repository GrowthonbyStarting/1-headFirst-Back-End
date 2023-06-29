package starting.growthon.controller;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    private final JavaMailSender javaMailSender;

    public MailController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @PostMapping("/send")
    public String sendMailTest() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            String[] receiveList = {"techofficer20@naver.com"};
            simpleMailMessage.setTo(receiveList);

            simpleMailMessage.setSubject("제목 부분");
            simpleMailMessage.setText("내용 부분");
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "SEND";
    }
}