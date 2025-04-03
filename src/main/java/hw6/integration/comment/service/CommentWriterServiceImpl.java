package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.dto.CommentCreateRequestDto;
import hw6.integration.comment.dto.CommentUpdateRequestDto;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.comment.util.CommentEqualsValidator;
import hw6.integration.comment.util.CommentReadValidator;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.util.PostDeletionValidator;
import hw6.integration.post.util.PostExistenceValidator;
import hw6.integration.user.domain.User;
import hw6.integration.user.util.UserEqualsValidator;
import hw6.integration.user.util.UserServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentWriterServiceImpl implements CommentWriterService {

    private final CommentWriteRepository commentWriteRepository;

    private final UserServiceValidator userServiceValidator;
    private final UserEqualsValidator userEqualsValidator;

    private final PostExistenceValidator postExistenceValidator;
    private final PostDeletionValidator postDeletionValidator;

    private final CommentReadValidator commentReadValidator;
    private final CommentEqualsValidator commentEqualsValidator;

    @Transactional
    @Override
    public Comment createComment(CommentCreateRequestDto dto, Long userId, Long postId) {

        User user = userServiceValidator.validateUserExists(userId);

        userEqualsValidator.validateUserActive(user);

        PostEntity postEntity = postExistenceValidator.validatePostEntityExists(postId);

        postDeletionValidator.validatePostEntityDeleted(postEntity);

        Comment comment = Comment.createComment(postId, userId, user.getNickname(), dto.getContent());

        Comment savedComment = commentWriteRepository.save(comment, user, postEntity);

        postEntity.incrementCommentCount();  // Dirty Checking으로 처리

        return savedComment; // domain 모델로 변환하여 반환
    }


    @Transactional
    @Override
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto, Long commentId, Long userId, Long postId) {

        User user = userServiceValidator.validateUserExists(userId);

        userEqualsValidator.validateUserActive(user);

        Post post = postExistenceValidator.validatePostExists(postId);

        postDeletionValidator.validatePostDeleted(post);

        CommentEntity commentEntity = commentReadValidator.validateCommentEntityExists(commentId);

        userEqualsValidator.validateUserAndCommentEntityEquals(userId, commentEntity.getUserEntity().getId());

        commentEntity.setContent(commentUpdateRequestDto.getContent());
        // Dirty Checking으로 바로 적용
    }


    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId, Long postId) {

        User user = userServiceValidator.validateUserExists(userId);

        userEqualsValidator.validateUserActive(user);

        PostEntity postEntity = postExistenceValidator.validatePostEntityExists(postId);

        postDeletionValidator.validatePostEntityDeleted(postEntity);

        CommentEntity commentEntity = commentReadValidator.validateCommentEntityExists(commentId);

        commentEqualsValidator.validateCommentEntityDeleted(commentEntity);

        userEqualsValidator.validateUserAndCommentEntityEquals(userId, commentEntity.getUserEntity().getId());

        commentEntity.setDeleted(true);
        postEntity.decrementCommentCount(); // Dirty Checking 사용
    }

}
