package hw6.integration.post.service;

import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.image.domain.Image;
import hw6.integration.image.entity.ImageEntity;
import hw6.integration.image.util.ImageValidator;
import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostUpdateRequestDto;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostWriteRepository;
import hw6.integration.post.util.PostDeletionValidator;
import hw6.integration.post.util.PostExistenceValidator;
import hw6.integration.user.domain.User;
import hw6.integration.user.util.UserEqualsValidator;
import hw6.integration.user.util.UserServiceValidator;
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
    private final CommentWriteRepository commentWriteRepository;

    private final ImageComponent imageComponent;

    private final UserServiceValidator userServiceValidator;
    private final UserEqualsValidator userEqualsValidator;

    private final PostExistenceValidator postExistenceValidator;
    private final PostDeletionValidator postDeletionValidator;

    private final ImageValidator imageValidator;

    //dirty checking을 위한 메서드
    @Transactional
    @Override
    public Post getPostById(Long id) {
        PostEntity postEntity = postExistenceValidator.validatePostEntityExists(id);

        postDeletionValidator.validatePostEntityDeleted(postEntity);

        postEntity.incrementViewCount(); // 엔티티에서 직접 메서드를 통해 증가 (Dirty Checking 활용)

        return postEntity.toDomain();
    }

    @Transactional
    @Override
    public Post createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {

        User user = userServiceValidator.validateUserExists(userId);

        userEqualsValidator.validateUserActive(user);

        Post post = Post.createPost(userId, postCreateRequestDto.getTitle(), postCreateRequestDto.getContent(), user.getNickname());

        List<Image> uploadImages = new ArrayList<>();
        if (postCreateRequestDto.getImages() != null && !postCreateRequestDto.getImages().isEmpty()) {

            imageValidator.validatorImageSize(postCreateRequestDto.getImages().size());

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
    public Post updatePost(Long postId, PostUpdateRequestDto postUpdateRequestDto, Long userId) {

        // 1. 사용자 유효성 검사
        User user = userServiceValidator.validateUserExists(userId);

        // 사용자 active 상태인지 검사
        userEqualsValidator.validateUserActive(user);

        // 2. 기존 게시글 엔티티 가져오기
        PostEntity postEntity = postExistenceValidator.validatePostEntityExists(postId);

        postDeletionValidator.validatePostEntityDeleted(postEntity);

        // 1. 제목/내용 업데이트 (메서드로 캡슐화)
        postEntity.update(postUpdateRequestDto.getTitle(), postUpdateRequestDto.getContent());

        // 4. 기존 이미지 가져오기
        List<ImageEntity> currentImages = postEntity.getImages();

        // 5. 삭제 요청 이미지 제거
        if (postUpdateRequestDto.getImagesToDelete() != null && !postUpdateRequestDto.getImagesToDelete().isEmpty()) {
            List<Long> toDelete = postUpdateRequestDto.getImagesToDelete();

            currentImages.removeIf(image -> {
                boolean shouldDelete = toDelete.contains(image.getId());
                if (shouldDelete) {
                    imageComponent.deleteImage(image.getImagePath()); // 실제 파일 삭제
                }
                return shouldDelete;
            });
        }

        // 6. 새 이미지 추가
        if (postUpdateRequestDto.getNewImages() != null && !postUpdateRequestDto.getNewImages().isEmpty()) {
            for (MultipartFile file : postUpdateRequestDto.getNewImages()) {
                String path = imageComponent.uploadPostImage(file);
                ImageEntity newImage = ImageEntity.builder()
                        .postEntity(postEntity)
                        .imagePath(path)
                        .build();
                currentImages.add(newImage);
            }
        }


        // 7. 이미지 개수 제한 검사
        imageValidator.validatorImageSize(currentImages.size());

        // 8. JPA는 영속 객체의 필드 변경만으로 업데이트 처리
        return postEntity.toDomain();

    }

    @Transactional
    @Override
    public void deletePost(Long userId, Long postId) {

        User user = userServiceValidator.validateUserExists(userId);

        userEqualsValidator.validateUserActive(user);

        PostEntity postEntity = postExistenceValidator.validatePostEntityExists(postId);

        postDeletionValidator.validatePostEntityDeleted(postEntity);

        if (postEntity.getCommentCount() > 0) {

            commentWriteRepository.deleteCommentByPostId(postId);
        }

        userEqualsValidator.validateUserAndPostEquals(userId, postEntity.getUserEntity().getId());

        postWriteRepository.deletePost(userId);

        //🔥 이미지 경로 순회하며 파일 삭제
//                if (post.getImages() != null) {
//                    post.getImages().forEach(image -> {
//                        imageComponent.deleteImage(image.getImagePath());
//                    });
//                }


    }
}
