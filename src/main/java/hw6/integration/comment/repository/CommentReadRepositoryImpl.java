package hw6.integration.comment.repository;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.entity.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentReadRepositoryImpl implements CommentReadRepository {

    private final CommentReadJpaRepository commentReadJpaRepository;

    @Override
    public List<Comment> findAllVisibleCommentsByPostId(Long postId) {
        return commentReadJpaRepository.findByIsDeletedFalse(postId)
                .stream()
                .map(CommentEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentReadJpaRepository.findById(commentId)
                .map(CommentEntity::toDomain);
    }

    @Override
    public Optional<CommentEntity> findByCommentEntityId(Long commentId) {
        return commentReadJpaRepository.findById(commentId);
    }

}
