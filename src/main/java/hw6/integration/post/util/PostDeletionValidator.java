package hw6.integration.post.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDeletionValidator {

    public void validatePostEntityDeleted(PostEntity postEntity) {

        if (postEntity.isDeleted())
            throw new BusinessException(ErrorCode.POST_DELETED);
    }

    public void validatePostDeleted(Post post) {

        if (post.isDeleted())
            throw new BusinessException(ErrorCode.POST_DELETED);
    }
}
