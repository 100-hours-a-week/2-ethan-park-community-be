package hw6.integration.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserTest {

    @Test
    @DisplayName("User 객체를 새로 생성하고 반환한다.")
    void should_return_user_when_create_user() {

        //given
        String email = "test@naver.com";
        String password = "password";
        String nickname = "test";
        String profilePath = "/image_profile/";

        //when
        User user = User.createUser(email, password, nickname, profilePath);

        //then
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(nickname, user.getNickname());
        assertEquals(profilePath, user.getProfilePath());
        assertTrue(user.getIsActive());

    }

    @Test
    void toEntity() {
    }

    @Test
    void isActiveUser() {
    }

    @Test
    void builder() {
    }
}