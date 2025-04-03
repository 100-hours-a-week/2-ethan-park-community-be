package hw6.integration.post.service;

import hw6.integration.post.domain.Post;
import hw6.integration.post.repository.PostReadRepository;
import hw6.integration.post.util.PostExistenceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadServiceImpl implements PostReadService {

    private final PostReadRepository postReadRepository;
    private final PostExistenceValidator postExistenceValidator;

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostByAll() {

        return postReadRepository.findAllVisiblePosts();
    }


    @Transactional(readOnly = true)
    @Override
    public Post findById(Long id) {
        return postExistenceValidator.validatePostExists(id);
    }

}
