package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.dto.CommentCreateRequestDto;
import hw6.integration.comment.dto.CommentUpdateRequestDto;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.comment.repository.CommentReadRepository;
import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.comment.util.CommentValidator;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.util.PostValidator;
import hw6.integration.user.domain.User;
import hw6.integration.user.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentWriterServiceImpl implements CommentWriterService {

    private final CommentWriteRepository commentWriteRepository;
    private final CommentReadRepository commentReadRepository;
    private final UserValidator userValidator;
    private final PostValidator postValidator;
    private final CommentValidator commentValidator;

    @Transactional
    @Override
    public Comment createComment(CommentCreateRequestDto dto, Long userId, Long postId) {

        User user = userValidator.validateUserExists(userId);

        userValidator.validateUserActive(user);

        PostEntity postEntity = postValidator.validatePostEntityExists(postId);

        postValidator.validatePostEntityDeleted(postEntity);

        Comment comment = Comment.createComment(postId, userId, user.getNickname(), dto.getContent());

        Comment savedComment = commentWriteRepository.save(comment, user, postEntity);

        postEntity.incrementCommentCount();  // Dirty Checking으로 처리

        return savedComment; // domain 모델로 변환하여 반환
    }


    @Transactional
    @Override
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto, Long commentId, Long userId, Long postId) {

        User user = userValidator.validateUserExists(userId);

        userValidator.validateUserActive(user);

        Post post = postValidator.validatePostExists(postId);

        postValidator.validatePostDeleted(post);

        CommentEntity commentEntity = commentValidator.validateCommentEntityExists(commentId);

        userValidator.validateUserAndCommentEntityEquals(userId, commentEntity.getUserEntity().getId());

        commentEntity.setContent(commentUpdateRequestDto.getContent());
        // Dirty Checking으로 바로 적용
    }


    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId, Long postId) {

        User user = userValidator.validateUserExists(userId);

        userValidator.validateUserActive(user);

        PostEntity postEntity = postValidator.validatePostEntityExists(postId);

        postValidator.validatePostEntityDeleted(postEntity);

        CommentEntity commentEntity = commentValidator.validateCommentEntityExists(commentId);

        commentValidator.validateCommentEntityDeleted(commentEntity);

        userValidator.validateUserAndCommentEntityEquals(userId, commentEntity.getUserEntity().getId());

        commentEntity.setDeleted(true);
        postEntity.decrementCommentCount(); // Dirty Checking 사용
    }

}
