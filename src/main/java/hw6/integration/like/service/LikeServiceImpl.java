package hw6.integration.like.service;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.like.repository.LikeRepository;
import hw6.integration.post.domain.Post;
import hw6.integration.post.repository.PostRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void toggleLike(Long userId, Long postId) {
        User user = findActiveUserById(userId);
        Post post = findActivePostById(postId);

        if (isLiked(userId, postId)) {
            cancelLike(userId, postId);
        } else {
            applyLike(userId, postId);
        }
    }

    private User findActiveUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    private Post findActivePostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND)).toDomain();
        if (post.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        return post;
    }

    private boolean isLiked(Long userId, Long postId) {
        return likeRepository.existsActiveLike(userId, postId) > 0;
    }

    private void cancelLike(Long userId, Long postId) {
        likeRepository.deactivateLike(userId, postId);
        postRepository.decrementLikeCount(postId);
    }

    private void applyLike(Long userId, Long postId) {
        likeRepository.insertLike(userId, postId);
        postRepository.incrementLikeCount(postId);
    }

}
