package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentReadRepository {

    List<Comment> findAllVisibleCommentsByPostId(Long postId);

    Optional<Comment> findById(Long commentId);

    Optional<CommentEntity> findByCommentEntityId(Long commentId);

}
