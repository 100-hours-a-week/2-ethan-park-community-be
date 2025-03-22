package hw6.integration.post.service;

import hw6.integration.comment.repository.CommentRepository;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.image.component.ImageComponent;
import hw6.integration.image.domain.Image;
import hw6.integration.image.entity.ImageEntity;
import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostUpdateRequestDto;
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

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ImageComponent imageComponent;

    @Override
    public List<Post> getPostByAll() {

        return postRepository.findAllVisiblePosts();
    }

    @Transactional
    @Override
    public Post getPostById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if(!post.isDeleted()) {
            postRepository.incrementViewCount(id);

            // 도메인 모델로 변경해서 가져오는 것으로 변경

            return post;
        }

        throw new BusinessException(ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    @Override
    public Post createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(user.getIsActive()) {

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
            return postRepository.save(post, User.toEntity(user));
        }

        throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }

    @Transactional
    @Override
    public Post updatePost(Long postId, PostUpdateRequestDto dto, Long userId) {

        // 1. 사용자 유효성 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(user.getIsActive()) {

            // 2. 기존 게시글 엔티티 가져오기
            PostEntity postEntity = postRepository.findEntityById(postId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

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
                return postEntity.toDomain(); // 변경 감지로 자동 update 됨
            }
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }

    @Transactional
    @Override
    public void deletePost(Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(user.getIsActive()) {

            PostEntity postEntity = postRepository.findEntityById(postId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

            if (!postEntity.isDeleted()) {

                if (postEntity.getComment_count() > 0) {

                    commentRepository.deleteCommentByPostId(postId);
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
            }

            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        throw new BusinessException(ErrorCode.USER_NOT_FOUND);

    }

}
