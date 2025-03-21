package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.post.entity.PostEntity;
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
    public List<Comment> findAllVisibleComments() {
        return commentJpaRepository.findByIsDeletedFalse()
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
    public Comment save(Comment comment, UserEntity userEntity, PostEntity postEntity) {
        return commentJpaRepository.save(Comment.toEntity(comment, postEntity, userEntity)).toDomain();
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
