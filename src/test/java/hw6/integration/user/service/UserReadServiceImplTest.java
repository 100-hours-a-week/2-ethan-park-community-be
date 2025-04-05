package hw6.integration.user.service;

import hw6.integration.exception.BusinessException;
import hw6.integration.exception.ErrorCode;
import hw6.integration.user.domain.User;
import hw6.integration.user.repository.UserReadRepository;
import hw6.integration.user.util.UserServiceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserReadServiceImplTest {

    @Mock
    private UserReadRepository userReadRepository;

    @Mock
    private UserServiceValidator userServiceValidator;

    @InjectMocks
    private UserReadServiceImpl userReadServiceImpl;

    @Test
    @DisplayName("모든 User 데이터를 가져온다")
    void should_return_user_list() {

        //given
        List<User> users = List.of(
                User.createUser("user1@naver.com", "password", "user1", "/profile1"),
                User.createUser("user2@naver.com", "password", "user2", "/profile2")
        );

        given(userReadServiceImpl.getUserByAll()).willReturn(users);

        //when
        List<User> result = userReadServiceImpl.getUserByAll();

        //then
        assertIterableEquals(users, result); // 순서와 값 비교
        verify(userReadRepository, times(1)).findByAll();

    }

    @Test
    @DisplayName("User 데이터가 없다면 Optional.empty를 반환한다.")
    void should_return_empty_when_user_is_empty() {

        //given
        List<User> emptyUsers = List.of();

        given(userReadRepository.findByAll()).willReturn(emptyUsers);

        //when
        List<User> result = userReadServiceImpl.getUserByAll();

        //then
        assertTrue(result.isEmpty()); // 순서와 값 비교
        verify(userReadRepository, times(1)).findByAll();

    }

    @Test
    @DisplayName("사용자 id를 조회했을 때 존재하면 User를 반환한다.")
    void should_return_user_when_user_id_exists() {

        //given
        Long userId = 1L;
        User user = User.createUser("test@naver.com", "password", "test", "/profile");
        given(userServiceValidator.validateUserExists(userId)).willReturn(user);

        //when
        User result = userReadServiceImpl.getUserById(userId);

        //then
        assertEquals(user, result);
        verify(userServiceValidator, times(1)).validateUserExists(userId);
    }

    @Test
    @DisplayName("사용자 id를 조회했을 때 존재하지 않으면 예외를 반환한다.")
    void should_throw_exception_when_user_id_is_not_exists() {

        //given
        Long userId = 999L;
        given(userServiceValidator.validateUserExists(userId)).willThrow(new BusinessException(ErrorCode.USER_NOT_FOUND));

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userReadServiceImpl.getUserById(userId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        verify(userServiceValidator, times(1)).validateUserExists(userId);
    }


}