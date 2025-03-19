package hw6.integration.image.component;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Component
public class ImageComponentImpl implements ImageComponent {

    private static final String BASE_UPLOAD_DIR = "/Users/park-youchan/Desktop/uploads";

    @Override
    public String uploadProfileImage(MultipartFile file) {
        return uploadImage(file, "profiles");
    }

    @Override
    public String uploadPostImage(MultipartFile file) {
        return uploadImage(file, "posts");
    }

    /**
     * 이미지 업로드 후 저장 경로 반환
     * @param file 업로드할 파일
     * @return 접근 가능한 이미지 경로
     */
    public String uploadImage(MultipartFile file, String type) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path targetPath = Paths.get(BASE_UPLOAD_DIR, type, uniqueFilename);

            // 디렉토리 없으면 생성
            Files.createDirectories(targetPath.getParent());

            // 파일 복사 (기존 파일이 있으면 덮어씀)
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 접근 가능한 경로로 변환
            return "/image_storage/" + type + "/" + uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    /**
     * 이미지 삭제
     * @param imagePath 접근 경로 (예: "/image_storage/uuid_filename.png")
     */
    @Override
    public void deleteImage(String imagePath) {
        try {
            String fileName = Paths.get(imagePath).getFileName().toString();
            String subFolder = imagePath.contains("profiles") ? "profiles" : "posts";
            Path fullPath = Paths.get(BASE_UPLOAD_DIR, subFolder, fileName);

            Files.deleteIfExists(fullPath);
            System.out.println("✅ 삭제 성공: " + fullPath.toString());

        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패", e);
        }
    }
}
