package starting.growthon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import starting.growthon.dto.response.FileDto;
import starting.growthon.entity.File;
import starting.growthon.repository.FileRepository;
import starting.growthon.repository.MentorInfoRepository;
import starting.growthon.util.S3Uploader;
import starting.growthon.util.UserUtil;

import java.io.IOException;

@Service
@Transactional
public class FileService {

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MentorInfoRepository mentorInfoRepository;

    @Autowired
    private UserUtil userUtil;

    // 모든 파일 사이즈 용량 제한은 5MB
    public FileDto profileUpload(MultipartFile file, File image) throws IOException {
        checkIsMentor();
        if (!file.isEmpty()) {
            deleteFileIfExisted("PROFILE", userUtil.getLoggedInUser().getId());
            String storedFileName = s3Uploader.outerUpload(file, "profiles");

            saveFileInfo(image, storedFileName, "PROFILE");

            fileRepository.save(image);
            return new FileDto(userUtil.getLoggedInUser().getName(), storedFileName, "PROFILE");
        }
        return null;
    }

    private void saveFileInfo(File file, String url, String type) {
        file.setUrl(url);
        file.setOwner(userUtil.getLoggedInUser());
        file.setType(type);
    }

    public FileDto mentorResumeUpload(MultipartFile file, File resume) throws IOException {
        checkIsMentor();
        if (!file.isEmpty()) {
            deleteFileIfExisted("RESUME", userUtil.getLoggedInUser().getId());
            String storedFileName = s3Uploader.outerUpload(file, "mentor-resume");
            saveFileInfo(resume, storedFileName, "RESUME");
            fileRepository.save(resume);
            mentorInfoRepository.findByMentorId(userUtil.getLoggedInUser().getId()).setVerified(true);
            return new FileDto(userUtil.getLoggedInUser().getName(), storedFileName, "RESUME");
        }
        return null;
    }

    private void deleteFileIfExisted(String type, Long mentorId) {
        File file = fileRepository.findByTypeAndOwnerId(type, mentorId);
        if (file != null) {
            String fileUrl = file.getUrl().substring(55);
            s3Uploader.deleteS3ObjectIfExisted(fileUrl);
            fileRepository.deleteById(file.getId());
        }
    }

    private void checkIsMentor() {
        if (!userUtil.getLoggedInUser().getRole().equals("MENTOR")) {
            throw new IllegalStateException("멘토가 아닙니다.");
        }
    }
}
