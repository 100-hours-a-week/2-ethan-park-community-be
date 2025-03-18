package hw6.integration.image.service;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.image.domain.Image;
import hw6.integration.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final ImageRepository imageRepository;
    private final String uploadDir = "/Users/park-youchan/Desktop/uploads";

    @Override
    public String uploadImage(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path path = Paths.get(uploadDir, uniqueFilename);

            // 디렉토리 없으면 생성
            Files.createDirectories(path.getParent());

            // 파일 저장
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 접근 가능한 경로로 반환 (예: "/images/uuid_파일명.png")
            return "/image_storage/" + uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    @Override
    public void deleteImage(String imagePath) {
        try {
            String fileName = Paths.get(imagePath).getFileName().toString();    // 파일명 추출
            Path path = Paths.get(uploadDir, fileName);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패", e);
        }
    }
}
