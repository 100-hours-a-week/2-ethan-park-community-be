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
        int exists = likeRepository.existsActiveLike(userId, postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(user.getIsActive()) {
            if (!post.isDeleted()) {
                if (exists > 0) {
                    likeRepository.deactivateLike(userId, postId);  // 좋아요 취소
                    postRepository.decrementLikeCount(postId);
                } else {
                    likeRepository.insertLike(userId, postId);      // 좋아요 등록
                    postRepository.incrementLikeCount(postId);
                }
            }

            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }
}
