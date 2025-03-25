package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentWriteRepositoryImpl implements CommentWriteRepository {

    private final CommentWriteJpaRepository commentWriteJpaRepository;
    private final CommentReadJpaRepository commentReadJpaRepository;


    @Override
    public Comment save(Comment comment, User user, PostEntity postEntity) {

        CommentEntity commentEntity = Comment.toEntity(comment,  postEntity, User.toEntity(user));

        return commentWriteJpaRepository.save(commentEntity).toDomain();
    }

    @Override
    public void updateAuthorName(Long userId, String newNickname) {
        List<CommentEntity> commentEntities = commentReadJpaRepository.findByUserEntity_Id(userId);
        for (CommentEntity comment : commentEntities) {
            comment.setAuthorName(newNickname); // 변경 감지
        }
    }

    @Override
    public void deleteCommentByPostId(Long postId) {
        commentWriteJpaRepository.deleteCommentByPostId(postId);
    }

    @Override
    public void deleteCommentByUserId(Long userId) {
        commentWriteJpaRepository.deleteCommentByUserId(userId);
        commentWriteJpaRepository.updateAuthorNameByUserId(userId);
    }


}
