package hw6.integration.like.service;

import hw6.integration.like.domain.Like;
import hw6.integration.like.repository.LikeRepository;
import hw6.integration.post.domain.Post;
import hw6.integration.post.repository.PostWriteRepository;
import hw6.integration.post.util.PostValidator;
import hw6.integration.user.domain.User;
import hw6.integration.user.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostWriteRepository postWriteRepository;
    private final UserValidator userValidator;
    private final PostValidator postValidator;

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
        User user = userValidator.validateUserExists(userId);

        userValidator.validateUserActive(user);

        return user;
    }

    private Post findActivePostById(Long postId) {
        Post post = postValidator.validatePostExists(postId);

        postValidator.validatePostDeleted(post);

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
