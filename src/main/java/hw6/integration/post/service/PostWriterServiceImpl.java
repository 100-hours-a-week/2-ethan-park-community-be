package hw6.integration.post.service;

import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.image.domain.Image;
import hw6.integration.image.entity.ImageEntity;
import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostUpdateRequestDto;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostReadRepository;
import hw6.integration.post.repository.PostWriteRepository;
import hw6.integration.post.util.PostValidator;
import hw6.integration.user.domain.User;
import hw6.integration.user.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostWriterServiceImpl implements PostWriterService {

    private final PostWriteRepository postWriteRepository;
    private final PostReadRepository postReadRepository;
    private final CommentWriteRepository commentWriteRepository;
    private final ImageComponent imageComponent;
    private final UserValidator userValidator;
    private final PostValidator postValidator;

    @Transactional
    @Override
    public Post createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {

        User user = userValidator.validateUserExists(userId);

        userValidator.validateUserActive(userId);

        Post post = Post.createPost(userId, postCreateRequestDto.getTitle(), postCreateRequestDto.getContent(), user.getNickname());

        List<Image> uploadImages = new ArrayList<>();
        if (postCreateRequestDto.getImages() != null && !postCreateRequestDto.getImages().isEmpty()) {
            if (postCreateRequestDto.getImages().size() > 10) {
                throw new BusinessException(ErrorCode.IMAGE_LIMIT_EXCEEDED);
            }

            for (MultipartFile file : postCreateRequestDto.getImages()) {
                String url = imageComponent.uploadPostImage(file);
                uploadImages.add(Image.builder().imagePath(url).build());
            }
        }

        post.addImages(uploadImages);
        return postWriteRepository.save(post, User.toEntity(user));
    }

    @Transactional
    @Override
    public Post updatePost(Long postId, PostUpdateRequestDto dto, Long userId) {

        // 1. 사용자 유효성 검사
        userValidator.validateUserExists(userId);

        // 사용자 active 상태인지 검사
        userValidator.validateUserActive(userId);

        // 2. 기존 게시글 엔티티 가져오기
        PostEntity postEntity = postValidator.validatePostEntityExists(postId);

        if (!postEntity.isDeleted()) {

            // 1. 제목/내용 업데이트 (메서드로 캡슐화)
            postEntity.update(dto.getTitle(), dto.getContent());

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
            return postEntity.toDomain();

        }
        throw new BusinessException(ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    @Override
    public void deletePost(Long userId, Long postId) {

        User user = userValidator.validateUserExists(userId);

        userValidator.validateUserActive(userId);

        PostEntity postEntity = postReadRepository.findEntityById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!postEntity.isDeleted()) {

            if (postEntity.getComment_count() > 0) {

                commentWriteRepository.deleteCommentByPostId(postId);
            }

            if (userId.equals(postEntity.getUserEntity().getId())) {
                postEntity.setDeleted(true);

            } else {
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }

            // 🔥 이미지 경로 순회하며 파일 삭제
            //        if (post.getImages() != null) {
            //            post.getImages().forEach(image -> {
            //                imageComponent.deleteImage(image.getImagePath());
            //            });
            //        }
        } else {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }


    }
}
