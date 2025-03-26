package hw6.integration.like.service;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.like.domain.Like;
import hw6.integration.like.repository.LikeRepository;
import hw6.integration.post.domain.Post;
import hw6.integration.post.repository.PostReadRepository;
import hw6.integration.post.repository.PostWriteRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostWriteRepository postWriteRepository;
    private final PostReadRepository postReadRepository;
    private final UserReadRepository userReadRepository;

    @Transactional
    @Override
    public void toggleLike(Long userId, Long postId) {
        User user = findActiveUserById(userId);
        Post post = findActivePostById(postId);

        if (isLiked(userId, postId)) {
            cancelLike(userId, postId);
        } else {
            applyLike(user, post);
        }
    }

    private User findActiveUserById(Long userId) {
        User user = userReadRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    private Post findActivePostById(Long postId) {
        Post post = postReadRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_DELETED);
        }
        return post;
    }

    @Override
    public boolean isLiked(Long userId, Long postId) {
        return likeRepository.existsActiveLike(userId, postId) > 0;
    }

    private void cancelLike(Long userId, Long postId) {
        likeRepository.deactivateLike(userId, postId);
        postWriteRepository.decrementLikeCount(postId);
    }

    private void applyLike(User user, Post post) {

        Like like = Like.createLike(user.getId(), post.getId());

        likeRepository.save(like, user, post);
        postWriteRepository.incrementLikeCount(post.getId());
    }

}
