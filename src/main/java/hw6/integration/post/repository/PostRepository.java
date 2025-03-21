package hw6.integration.post.repository;

import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(Long id);

    Post save(Post post, UserEntity userEntity);

    Optional<PostEntity> findEntityById(Long id);

    public void updateAuthorName(Long userId, String newNickname);

    List<Post> findAllVisiblePosts();

    void incrementViewCount(Long id);

    void incrementContentCount(Long id);

    void decrementContentCount(Long id);

    void incrementLikeCount(Long id);

    void decrementLikeCount(Long id);

    void deletePostByUserId(Long userId, boolean authorName, String deletedUser);
}
