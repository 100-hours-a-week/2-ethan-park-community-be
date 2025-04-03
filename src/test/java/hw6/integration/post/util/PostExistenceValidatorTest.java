package hw6.integration.post.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import hw6.integration.post.repository.PostReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PostExistenceValidatorTest {

    private PostReadRepository postReadRepository;
    private PostExistenceValidator postExistenceValidator;

    @BeforeEach
    void setUp() {
        postReadRepository = mock(PostReadRepository.class);
        postExistenceValidator = new PostExistenceValidator(postReadRepository);
    }

    @Test
    @DisplayName("postId가 존재하면 PostEntity가 반환되어야 한다.")
    void should_return_post_entitiy_when_post_entity_exists() {
        Long postId = 1L;
        PostEntity postEntity = mock(PostEntity.class);
        given(postReadRepository.findEntityById(postId)).willReturn(Optional.of(postEntity));

        //when
        PostEntity result = postExistenceValidator.validatePostEntityExists(postId);

        //then
        assertThat(result).isEqualTo(postEntity);
        verify(postReadRepository, times(1)).findEntityById(postId);
    }

    @Test
    @DisplayName("postId가 존재하지 않으면 예외가 반환되어야 한다.")
    void should_throw_exception_when_post_entity_is_not_exists() {

        //given
        Long postId = 999L;
        given(postReadRepository.findEntityById(postId)).willReturn(Optional.empty());

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            postExistenceValidator.validatePostEntityExists(postId);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.POST_NOT_FOUND);
    }


    @Test
    @DisplayName("postId가 존재하면 Post가 반환되어야 한다.")
    void should_return_post_when_postid_exists() {

        //given
        Long postId = 1L;
        Post post = mock(Post.class);
        given(postReadRepository.findById(postId)).willReturn(Optional.of(post));

        //when
        Post result = postExistenceValidator.validatePostExists(postId);

        //then
        assertThat(result).isEqualTo(post);
        verify(postReadRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("postId가 존재하지 않으면 예외를 반환해야 한다.")
    void should_throw_exception_when_post_is_not_exists() {

        //given
        Long postId = 999L;
        given(postReadRepository.findById(postId)).willReturn(Optional.empty());

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            postExistenceValidator.validatePostExists(postId);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

}