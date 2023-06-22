package starting.growthon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starting.growthon.exception.ExceptionResponse;
import starting.growthon.exception.TargetNotFoundException;
import starting.growthon.service.FollowService;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;


    @PostMapping("/{mentorId}")
    public ResponseEntity<?> addFollow(@PathVariable Long mentorId) {
        try {
            return ResponseEntity.ok(followService.addFollow(mentorId));
        } catch (IllegalStateException | TargetNotFoundException e) {
            return errorMessage(e);
        }
    }

    @DeleteMapping("/{mentorId}")
    public ResponseEntity<?> cancelFollow(@PathVariable Long mentorId) {
        try {
            followService.cancelFollow(mentorId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (IllegalStateException | TargetNotFoundException e) {
            return errorMessage(e);
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(RuntimeException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
