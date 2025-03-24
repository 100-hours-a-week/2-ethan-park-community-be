package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.domain.User;
import hw6.integration.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findAllVisibleCommentsByPostId(Long postId);

    Optional<Comment> findById(Long commentId);

    Comment save(Comment comment, User user, PostEntity postEntity);

    Optional<CommentEntity> findByCommentEntityId(Long commentId);

    void updateAuthorName(Long userId, String newNickname);

    void deleteCommentByPostId(Long postId);

    void deleteCommentByUserId(Long userId);
}
