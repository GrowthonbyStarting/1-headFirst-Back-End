package starting.growthon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import starting.growthon.exception.ExceptionResponse;
import starting.growthon.exception.NotLoggedInException;
import starting.growthon.service.UserService;

@RestController("/")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("test page");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        try {
            return ResponseEntity.ok(userService.profile());
        } catch (NotLoggedInException e) {
            return errorMessage(e);
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(RuntimeException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
