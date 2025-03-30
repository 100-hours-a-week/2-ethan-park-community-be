package hw6.integration.post.repository;

import hw6.integration.post.domain.Post;
import hw6.integration.user.entity.UserEntity;

public interface PostWriteRepository {

    Post save(Post post, UserEntity userEntity);

    void updateAuthorName(Long userId, String newNickname);

    void incrementLikeCount(Long id);

    void decrementLikeCount(Long id);

    void deletePostByUserId(Long userId);

    void deletePost(Long userId);
}
