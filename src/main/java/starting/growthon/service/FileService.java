package starting.growthon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import starting.growthon.dto.response.FileDto;
import starting.growthon.entity.File;
import starting.growthon.repository.FileRepository;
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
    private UserUtil userUtil;

    public FileDto profileUpload(MultipartFile file, File image) throws IOException {
        checkIsMentor();
        if (!file.isEmpty()) {
            deleteProfileImageIfExisted(userUtil.getLoggedInUser().getId());
            String storedFileName = s3Uploader.outerUpload(file, "profiles");
            image.setUrl(storedFileName);
            image.setOwner(userUtil.getLoggedInUser());
            image.setType("PROFILE");
            fileRepository.save(image);
            return new FileDto(userUtil.getLoggedInUser().getName(), storedFileName, "PROFILE");
        }
        return null;
    }

    private void deleteProfileImageIfExisted(Long mentorId) {
        File img = fileRepository.findByTypeAndOwnerId("PROFILE", mentorId);
        if (img != null) {
            String fileUrl = img.getUrl().substring(55);
            s3Uploader.deleteS3ObjectIfExisted(fileUrl);
            fileRepository.deleteById(img.getId());
        }
    }

    private void checkIsMentor() {
        if (!userUtil.getLoggedInUser().getRole().equals("MENTOR")) {
            throw new IllegalStateException("멘토가 아닙니다.");
        }
    }
}
