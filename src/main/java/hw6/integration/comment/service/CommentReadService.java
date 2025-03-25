package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;

import java.util.List;

public interface CommentReadService {

    List<Comment> getCommentByPostId(Long postId);

}
