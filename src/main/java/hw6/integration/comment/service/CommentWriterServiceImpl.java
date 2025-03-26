package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.dto.CommentCreateRequestDto;
import hw6.integration.comment.dto.CommentUpdateRequestDto;
import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.comment.repository.CommentReadRepository;
import hw6.integration.comment.repository.CommentWriteRepository;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostReadRepository;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentWriterServiceImpl implements CommentWriterService {

    private final CommentWriteRepository commentWriteRepository;
    private final CommentReadRepository commentReadRepository;
    private final UserReadRepository userReadRepository;
    private final PostReadRepository postReadRepository;

    @Transactional
    @Override
    public Comment createComment(CommentCreateRequestDto dto, Long userId, Long postId) {

        User user = userReadRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        PostEntity postEntity = postReadRepository.findEntityById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (postEntity.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_DELETED);
        }

        Comment comment = Comment.createComment(postId, userId, user.getNickname(), dto.getContent());

        Comment savedComment = commentWriteRepository.save(comment, user, postEntity);

        postEntity.incrementCommentCount();  // Dirty Checking으로 처리

        return savedComment; // domain 모델로 변환하여 반환
    }


    @Transactional
    @Override
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto, Long commentId, Long userId, Long postId) {

        User user = userReadRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Post post = postReadRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (post.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_DELETED);
        }

        CommentEntity commentEntity = commentReadRepository.findByCommentEntityId(commentId)
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

        User user = userReadRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        if (!user.getIsActive()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        PostEntity postEntity = postReadRepository.findEntityById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (postEntity.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_DELETED);
        }

        CommentEntity commentEntity = commentReadRepository.findByCommentEntityId(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (commentEntity.isDeleted()) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!userId.equals(commentEntity.getUserEntity().getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        commentEntity.setDeleted(true);
        postEntity.decrementCommentCount(); // Dirty Checking 사용
    }

}
