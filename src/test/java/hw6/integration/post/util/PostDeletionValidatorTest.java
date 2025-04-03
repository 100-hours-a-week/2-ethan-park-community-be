package hw6.integration.post.util;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.post.domain.Post;
import hw6.integration.post.entity.PostEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PostDeletionValidatorTest {

    private PostDeletionValidator postDeletionValidator;

    @BeforeEach
    void setUp() {
        postDeletionValidator = new PostDeletionValidator();
    }

    @Test
    @DisplayName("postEntity의 isDeleted가 false면 예외를 반환하지 않는다.")
    void should_return_nothing_when_post_entity_deleted_false() {

        //given
        PostEntity postEntity = mock(PostEntity.class);
        given(postEntity.isDeleted()).willReturn(false);

        //then
        assertDoesNotThrow(() -> postDeletionValidator.validatePostEntityDeleted(postEntity));

    }

    @Test
    @DisplayName("postEntity의 isDeleted가 true면 예외를 반환한다.")
    void should_return_nothing_when_post_entity_deleted_true() {

        //given
        PostEntity postEntity = mock(PostEntity.class);
        given(postEntity.isDeleted()).willReturn(true);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            postDeletionValidator.validatePostEntityDeleted(postEntity);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.POST_DELETED);

    }

    @Test
    @DisplayName("post의 isDeleted가 false면 예외를 반환하지 않는다.")
    void should_return_nothing_when_post_deleted_false() {

        //given
        Post post = mock(Post.class);
        given(post.isDeleted()).willReturn(false);

        //then
        assertDoesNotThrow(() -> postDeletionValidator.validatePostDeleted(post));
    }

    @Test
    @DisplayName("post의 isDeleted가 true면 예외를 반환한다.")
    void should_throw_exception_when_post_deleted_true() {

        //given
        Post post = mock(Post.class);
        given(post.isDeleted()).willReturn(true);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            postDeletionValidator.validatePostDeleted(post);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.POST_DELETED);
    }

}