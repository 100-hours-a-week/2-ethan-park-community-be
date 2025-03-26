package hw6.integration.post.service;

import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostReadRepository;
import hw6.integration.post.util.PostValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadServiceImpl implements PostReadService {

    private final PostReadRepository postReadRepository;
    private final PostValidator postValidator;

    @Override
    public List<Post> getPostByAll() {

        return postReadRepository.findAllVisiblePosts();
    }

    //dirty checking을 위한 메서드
    @Transactional
    @Override
    public Post getPostById(Long id) {
        PostEntity postEntity = postValidator.validatePostEntityExists(id);

        postValidator.validatePostEntityDeleted(postEntity);

        postEntity.incrementViewCount(); // 엔티티에서 직접 메서드를 통해 증가 (Dirty Checking 활용)

        return postEntity.toDomain();
    }

    @Override
    public Post findById(Long id) {
        return postValidator.validatePostExists(id);
    }


}
