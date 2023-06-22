package starting.growthon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import starting.growthon.service.MentorService;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    /*
    @PostMapping("/info")
    public ResponseEntity<?> infoUpload() {
        return ResponseEntity.ok(mentorService.uploadInfo());
    }

    @PostMapping("/profile-image")
    public ResponseEntity<?> uploadMentorImage() {
        return ResponseEntity.ok(mentorService.uploadProfileImage());
    }
     */
}
