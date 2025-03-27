package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentReadService {

    @Transactional
    List<Comment> getCommentByPostId(Long postId);

}
