package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.domain.User;
import hw6.integration.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public List<Comment> findAllVisibleCommentsByPostId(Long postId) {
        return commentJpaRepository.findByIsDeletedFalse(postId)
                .stream()
                .map(CommentEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .map(CommentEntity::toDomain);
    }

    @Override
    public Comment save(Comment comment, User user, PostEntity postEntity) {

        CommentEntity commentEntity = Comment.toEntity(comment,  postEntity, User.toEntity(user));

        return commentJpaRepository.save(commentEntity).toDomain();
    }

    @Override
    public Optional<CommentEntity> findByCommentEntityId(Long commentId) {
        return commentJpaRepository.findById(commentId);
    }

    @Override
    public void updateAuthorName(Long userId, String newNickname) {
        List<CommentEntity> commentEntities = commentJpaRepository.findByUserEntity_Id(userId);
        for (CommentEntity comment : commentEntities) {
            comment.setAuthorName(newNickname); // ✅ 변경 감지
        }
    }

    @Override
    public void deleteCommentByPostId(Long postId) {
        commentJpaRepository.deleteCommentByPostId(postId);
    }

    @Override
    public void deleteCommentByUserId(Long userId) {
        commentJpaRepository.deleteCommentByUserId(userId);
        commentJpaRepository.updateAuthorNameByUserId(userId);
    }


}
