package hw6.integration.post.service;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.image.domain.Image;
import hw6.integration.image.entity.ImageEntity;
import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostUpdateDto;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageComponent imageComponent;

    @Override
    public List<Post> getPostByAll() {
        return postRepository.findByAll()
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional
    @Override
    public Post createPost(String title, String content, List<MultipartFile> images, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.createPost(userId, title, content, user.getNickname());

        List<Image> uploadImages = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            if (images.size() > 10) {
                throw new BusinessException(ErrorCode.IMAGE_LIMIT_EXCEEDED);
            }

            for (MultipartFile file : images) {
                String url = imageComponent.uploadPostImage(file);
                uploadImages.add(Image.builder().imagePath(url).build());
            }
        }

        post.addImages(uploadImages);
        return postRepository.save(post, User.toEntity(user));
    }

    @Transactional
    @Override
    public Post updatePost(Long postId, PostUpdateDto dto, Long userId) {

        // 1. 사용자 유효성 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 기존 게시글 엔티티 가져오기
        PostEntity postEntity = postRepository.findEntityById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 3. 제목/내용 업데이트
        if (dto.getTitle() != null) {
            postEntity.setTitle(dto.getTitle());
        }

        if (dto.getContent() != null) {
            postEntity.setContent(dto.getContent());
        }

        // 4. 기존 이미지 가져오기
        List<ImageEntity> currentImages = postEntity.getImages();

        // 5. 삭제 요청 이미지 제거
        if (dto.getImagesToDelete() != null && !dto.getImagesToDelete().isEmpty()) {
            List<Long> toDelete = dto.getImagesToDelete();

            currentImages.removeIf(image -> {
                boolean shouldDelete = toDelete.contains(image.getId());
                if (shouldDelete) {
                    imageComponent.deleteImage(image.getImagePath()); // 실제 파일 삭제
                }
                return shouldDelete;
            });
        }

        // 6. 새 이미지 추가
        if (dto.getNewImages() != null && !dto.getNewImages().isEmpty()) {
            for (MultipartFile file : dto.getNewImages()) {
                String path = imageComponent.uploadPostImage(file);
                ImageEntity newImage = ImageEntity.builder()
                        .postEntity(postEntity)
                        .imagePath(path)
                        .build();
                currentImages.add(newImage);
            }
        }

        // 7. 이미지 개수 제한 검사
        if (currentImages.size() > 10) {
            throw new BusinessException(ErrorCode.IMAGE_LIMIT_EXCEEDED);
        }

        // 8. JPA는 영속 객체의 필드 변경만으로 업데이트 처리
        return postEntity.toDomain(); // 변경 감지로 자동 update 됨
    }

    @Override
    public void deletePost(Long userId, Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        if(userId.equals(post.getUserId())) {
            postRepository.delete(postId);
        } else {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // 🔥 이미지 경로 순회하며 파일 삭제
        if (post.getImages() != null) {
            post.getImages().forEach(image -> {
                imageComponent.deleteImage(image.getImagePath());
            });
        }

    }

}
