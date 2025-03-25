package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.dto.CommentCreateRequestDto;
import hw6.integration.comment.dto.CommentUpdateRequestDto;

public interface CommentWriterService {


    Comment createComment(CommentCreateRequestDto commentCreateDto, Long userId, Long postId);

    void updateComment(CommentUpdateRequestDto commentUpdateRequestDto, Long commentId, Long userId, Long postId);

    void deleteComment(Long commentId, Long userId, Long postId);
}
