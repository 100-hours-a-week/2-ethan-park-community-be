package hw6.integration.comment.service;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.repository.CommentReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReadServiceImpl implements CommentReadService {

    private final CommentReadRepository commentReadRepository;

    @Override
    public List<Comment> getCommentByPostId(Long postId) {
        return commentReadRepository.findAllVisibleCommentsByPostId(postId)
                .stream()
                .toList();
    }
}
