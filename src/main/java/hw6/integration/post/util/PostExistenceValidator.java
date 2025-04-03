package hw6.integration.post.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostValidator {

    private final PostReadRepository postReadRepository;

    public PostEntity validatePostEntityExists(Long postId) {

        return postReadRepository.findEntityById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public Post validatePostExists(Long postId) {

        return postReadRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public void validatePostEntityDeleted(PostEntity postEntity) {

        if (postEntity.isDeleted())
            throw new BusinessException(ErrorCode.POST_DELETED);
    }

    public void validatePostDeleted(Post post) {

        if (post.isDeleted())
            throw new BusinessException(ErrorCode.POST_DELETED);
    }
}
