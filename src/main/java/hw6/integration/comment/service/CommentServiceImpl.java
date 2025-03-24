package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.dto.CommentCreateRequestDto;
import hw6.integration.comment.dto.CommentUpdateRequestDto;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.comment.repository.CommentRepository;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.entity.UserEntity;
import hw6.integration.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public List<Comment> getCommentByPostId(Long postId) {
        return commentRepository.findAllVisibleCommentsByPostId(postId)
                .stream()
                .toList();
    }

    @Transactional
    @Override
    public Comment createComment(CommentCreateRequestDto dto, Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (postEntity.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Comment comment = Comment.createComment(postId, userId, user.getNickname(), dto.getContent());

        Comment savedComment = commentRepository.save(comment, user, postEntity);

        postEntity.incrementCommentCount();  // Dirty Checking으로 처리

        return savedComment; // domain 모델로 변환하여 반환
    }


    @Transactional
    @Override
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto, Long commentId, Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (post.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        CommentEntity commentEntity = commentRepository.findByCommentEntityId(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!userId.equals(commentEntity.getUserEntity().getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        commentEntity.setContent(commentUpdateRequestDto.getContent());
        // Dirty Checking으로 바로 적용
    }


    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (post.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        CommentEntity commentEntity = commentRepository.findByCommentEntityId(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (commentEntity.isDeleted()) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!userId.equals(commentEntity.getUserEntity().getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        commentEntity.setDeleted(true);
        post.decrementCommentCount(); // Dirty Checking 사용
    }

}
