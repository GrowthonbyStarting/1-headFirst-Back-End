package starting.growthon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import starting.growthon.dto.MentorInfoRequestDto;
import starting.growthon.entity.File;
import starting.growthon.exception.ExceptionResponse;
import starting.growthon.service.FileService;
import starting.growthon.service.MentorService;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MentorService mentorService;

    @PostMapping("/info")
    public ResponseEntity<?> infoUpload(@RequestBody MentorInfoRequestDto mentorInfoRequestDto) {
        try {
            return ResponseEntity.ok(mentorService.infoUpload(mentorInfoRequestDto));
        } catch (IllegalStateException e) {
            return errorMessage(e);
        }
    }

    @PostMapping(value = "/profile-image", consumes =
            MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> profileUpload(@RequestParam(value = "image")
                                           MultipartFile file, File image) throws Exception {
        try {
            return ResponseEntity.ok(fileService.profileUpload(file, image));
        } catch (IllegalStateException e) {
            return errorMessage(e);
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(RuntimeException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
