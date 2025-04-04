package hw6.integration.user.service;

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

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        Long userId = 999L;
        List<User> emptyUsers = List.of();

        given(userReadRepository.findByAll()).willReturn(emptyUsers);

        //when
        List<User> result = userReadServiceImpl.getUserByAll();

        //then
        assertTrue(result.isEmpty()); // 순서와 값 비교
        verify(userReadRepository, times(1)).findByAll();

    }

    //getUserById 메서드 테스트 해야됨
}