package hw6.integration.service.post;

import hw6.integration.domain.model.Post;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.exception.PostNotFoundException;
import hw6.integration.repository.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public List<Post> getPostByAll() {
        return postRepository.findByAll()
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }
}
