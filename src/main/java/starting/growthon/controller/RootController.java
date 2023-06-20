package starting.growthon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import starting.growthon.entity.MentorInfo;
import starting.growthon.exception.ExceptionResponse;
import starting.growthon.exception.NotLoggedInException;
import starting.growthon.service.MentorService;
import starting.growthon.service.UserService;

import java.util.List;

@RestController("/")
public class RootController {

    private final UserService userService;
    private final MentorService mentorService;

    public RootController(UserService userService, MentorService mentorService) {
        this.userService = userService;
        this.mentorService = mentorService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("test page (edit test)");
    }

    @GetMapping("/main")
    public ResponseEntity<List<MentorInfo>> main() {
        return ResponseEntity.ok(mentorService.getMentors());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MentorInfo>> search(@RequestParam String condition) {
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

    private static ResponseEntity<ExceptionResponse> errorMessage(RuntimeException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
