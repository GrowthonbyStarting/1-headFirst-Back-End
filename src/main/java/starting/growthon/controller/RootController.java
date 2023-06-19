package starting.growthon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import starting.growthon.dto.response.UserInfoDto;
import starting.growthon.exception.ExceptionResponse;
import starting.growthon.exception.NotLoggedInException;
import starting.growthon.service.UserService;

import java.util.List;

@RestController("/")
public class RootController {

    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("test page (edit test)");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        try {
            return ResponseEntity.ok(userService.profile());
        } catch (NotLoggedInException e) {
            return errorMessage(e);
        }
    }

    @GetMapping("/main")
    public ResponseEntity<List<UserInfoDto>> mainRoom() {
        // 모든 멘토들이 식별되어야 함 (조건 없다고 가정)
        return ResponseEntity.ok(userService.findMentors());
    }

    @PostMapping("/role")
    public ResponseEntity<?> changeRole() {
        try {
            return ResponseEntity.ok(userService.changeRole());
        } catch (NotLoggedInException e) {
            return errorMessage(e);
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(RuntimeException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
