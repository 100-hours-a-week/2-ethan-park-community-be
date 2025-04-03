package hw6.integration.comment.util;

import hw6.integration.comment.entity.CommentEntity;
import hw6.integration.comment.repository.CommentReadRepository;
import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CommentReadValidatorTest {

    private CommentReadRepository commentReadRepository;
    private CommentReadValidator commentReadValidator;

    @BeforeEach
    void setUp() {
        commentReadRepository = mock(CommentReadRepository.class);
        commentReadValidator = new CommentReadValidator(commentReadRepository);
    }

    @Test
    @DisplayName("commentId가 존재하면 commentEntity를 반환한다.")
    void should_return_comment_entity_when_comment_id_exists() {

        //given
        Long commentId = 1L;
        CommentEntity commentEntity = mock(CommentEntity.class);
        given(commentReadRepository.findByCommentEntityId(commentId)).willReturn(Optional.of(commentEntity));

        //when
        CommentEntity result = commentReadValidator.validateCommentEntityExists(commentId);

        //then
        assertThat(result).isEqualTo(commentEntity);
        verify(commentReadRepository, times(1)).findByCommentEntityId(commentId);
    }

    @Test
    @DisplayName("commentId가 존재하지 않으면 예외를 반환한다.")
    void should_throw_exception_when_comment_id_is_not_exists() {

        //given
        Long commentId = 999L;
        given(commentReadRepository.findByCommentEntityId(commentId)).willReturn(Optional.empty());

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            commentReadValidator.validateCommentEntityExists(commentId);
        });

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
        verify(commentReadRepository, times(1)).findByCommentEntityId(commentId);
    }

}