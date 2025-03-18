package hw6.integration.image.service;

import org.springframework.web.multipart.MultipartFile;


public interface ImageService {


    public String uploadImage(MultipartFile file);

    public void deleteImage(String imagePath);

}