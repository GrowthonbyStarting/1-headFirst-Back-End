package starting.growthon.controller;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starting.growthon.dto.MentoringDto;
import starting.growthon.dto.response.MentorInfoResponseDto;
import starting.growthon.exception.ExceptionResponse;
import starting.growthon.exception.NotLoggedInException;
import starting.growthon.service.MenteeService;
import starting.growthon.service.MentorService;

import java.util.List;

@RestController("/")
public class RootController {
    private final MentorService mentorService;
    private final MenteeService menteeService;

    public RootController(MentorService mentorService, MenteeService menteeService) {
        this.mentorService = mentorService;
        this.menteeService = menteeService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("test page (edit test)");
    }

    @GetMapping("/main")
    public ResponseEntity<List<MentorInfoResponseDto>> main() {
        return ResponseEntity.ok(mentorService.getMentors());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MentorInfoResponseDto>> search(@RequestParam String condition) {
        return ResponseEntity.ok(mentorService.mentorSearch(condition));
    }

    @PostMapping("/role")
    public ResponseEntity<?> changeRole() {
        try {
            return ResponseEntity.ok(mentorService.changeRole());
        } catch (NotLoggedInException e) {
            return errorMessage(e);
        }
    }

    @PostMapping("/mentoring/{uuid}")
    public ResponseEntity<?> requestMentoring(@RequestBody MentoringDto mentoringDto, @PathVariable Long uuid) {
        try {
            return ResponseEntity.ok(menteeService.mentoring(mentoringDto, uuid));
        } catch (MessagingException e) {
            return errorMessage(new RuntimeException(e));
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(RuntimeException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
