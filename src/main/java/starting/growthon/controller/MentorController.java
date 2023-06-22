package starting.growthon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import starting.growthon.entity.File;
import starting.growthon.exception.ExceptionResponse;
import starting.growthon.service.FileService;

@RestController
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    private FileService fileService;

    /*
    @PostMapping("/info")
    public ResponseEntity<?> infoUpload() {
        return ResponseEntity.ok(mentorService.uploadInfo());
    }
     */

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
