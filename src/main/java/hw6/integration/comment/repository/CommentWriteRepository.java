package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.domain.User;

public interface CommentWriteRepository {


    Comment save(Comment comment, User user, PostEntity postEntity);

    void updateAuthorName(Long userId, String newNickname);

    void deleteCommentByPostId(Long postId);

    void deleteCommentByUserId(Long userId);
}
