package hw6.integration.post.repository;

import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.domain.Post;
import hw6.integration.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostWriteRepositoryImpl implements PostWriteRepository {

    private final PostWriteJpaRepository postWriteJpaRepository;
    private final PostReadJpaRepository postReadJpaRepository;

    @Override
    public Post save(Post post, UserEntity userEntity) {

        PostEntity postEntity = Post.toEntity(post, userEntity);
        PostEntity saved = postWriteJpaRepository.save(postEntity);

        return saved.toDomain();
    }

    @Override
    public void updateAuthorName(Long userId, String newNickname) {
        List<PostEntity> postEntities = postReadJpaRepository.findByUserEntity_Id(userId);
        for (PostEntity post : postEntities) {
            post.setAuthorName(newNickname); // ✅ 변경 감지
        }
    }

    @Override
    public void incrementLikeCount(Long id) {
        postWriteJpaRepository.incrementLikeCount(id);
    }

    @Override
    public void decrementLikeCount(Long id) {
        postWriteJpaRepository.decrementLikeCount(id);
    }

    @Override
    public void deletePostByUserId(Long userId) {
        postWriteJpaRepository.deletePostByUserId(userId);
        postWriteJpaRepository.updateAuthorNameByUserId(userId);
    }
}
