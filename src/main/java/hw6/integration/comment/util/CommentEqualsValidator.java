package hw6.integration.comment.util;

import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEqualsValidator {

    public void validateCommentEntityDeleted(CommentEntity commentEntity) {

        if (commentEntity.isDeleted())
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
    }
}
