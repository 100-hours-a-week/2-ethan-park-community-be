package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.dto.CommentUpdateRequestDto;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findAllVisibleComments();

    Optional<Comment> findById(Long commentId);

    Comment save(Comment comment, UserEntity userEntity, PostEntity postEntity);

    Optional<CommentEntity> findByCommentEntityId(Long commentId);

    void updateAuthorName(Long userId, String newNickname);

    void deleteCommentByPostId(Long postId, boolean delete);

    void deleteCommentByUserId(Long userId, boolean delete, String deletedUser);
}
