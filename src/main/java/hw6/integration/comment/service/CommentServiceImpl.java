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
    public List<Comment> getCommentByAll() {
        return commentRepository.findAllVisibleComments()
                .stream()
                .toList();
    }

    @Transactional
    @Override
    public Comment createComment(CommentCreateRequestDto commentCreateRequestDto, Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        UserEntity userEntity = User.toEntity(user);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        PostEntity postEntity = Post.toEntity(post, userEntity);

        Comment comment = Comment.createComment(
                userId,
                postId,
                user.getNickname(),
                commentCreateRequestDto.getContent()
        );

        postRepository.incrementContentCount(postId);

        return commentRepository.save(comment, userEntity, postEntity);

    }

    @Transactional
    @Override
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto, Long commentId, Long userId, Long postId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        postRepository.findEntityById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));


        CommentEntity commentEntity = commentRepository.findByCommentEntityId(commentId).orElseThrow();
        commentEntity.setContent(commentUpdateRequestDto.getContent());
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        CommentEntity commentEntity = commentRepository.findByCommentEntityId(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if(userId.equals(commentEntity.getUserEntity().getId()) && postId.equals(commentEntity.getPostEntity().getId())) {
            commentEntity.setDeleted(true);

        } else {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

    }
}
