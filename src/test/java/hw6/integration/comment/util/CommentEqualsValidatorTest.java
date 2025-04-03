package hw6.integration.comment.util;

import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class CommentEqualsValidatorTest {

    private CommentEqualsValidator commentEqualsValidator;

    @BeforeEach
    void setUp() {
        commentEqualsValidator = new CommentEqualsValidator();
    }

    @Test
    @DisplayName("commentEntity의 isDeleted가 false면 예외를 반환하지 않는다.")
    void should_return_nothing_when_comment_entity_deleted_false() {

        //given
        CommentEntity commentEntity = mock(CommentEntity.class);
        given(commentEntity.isDeleted()).willReturn(false);

        //then
        assertDoesNotThrow(() -> commentEqualsValidator.validateCommentEntityDeleted(commentEntity));
    }

    @Test
    @DisplayName("commentEntity의 isDeleted가 true면 예외를 반환한다.")
    void should_throw_exception_when_comment_entity_deleted_true() {

        //given
        CommentEntity commentEntity = mock(CommentEntity.class);
        given(commentEntity.isDeleted()).willReturn(true);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            commentEqualsValidator.validateCommentEntityDeleted(commentEntity);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
    }

}