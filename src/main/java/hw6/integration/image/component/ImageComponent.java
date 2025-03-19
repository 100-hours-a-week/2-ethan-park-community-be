package hw6.integration.image.component;

import org.springframework.web.multipart.MultipartFile;


public interface ImageComponent {

    String uploadProfileImage(MultipartFile file);
    String uploadPostImage(MultipartFile file);
    void deleteImage(String imagePath);

}