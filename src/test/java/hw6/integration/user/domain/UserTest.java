package hw6.integration.user.domain;

import hw6.integration.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


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
    @DisplayName("email이 null이라면 User 객체를 새로 생성하지 않고 예외를 반환한다.")
    void should_throw_exception_when_email_null() {

        //given
        String email = null;
        String password = "password";
        String nickname = "test";
        String profilePath = "/image_profile/";

        //when & then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(email, password, nickname, profilePath);
        });
    }

    @Test
    @DisplayName("password이 null이라면 User 객체를 새로 생성하지 않고 예외를 반환한다.")
    void should_throw_exception_when_password_null() {

        //given
        String email = "test@naver.com";
        String password = null;
        String nickname = "test";
        String profilePath = "/image_profile/";

        //when & then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(email, password, nickname, profilePath);
        });
    }

    @Test
    @DisplayName("nickname이 null이라면 User 객체를 새로 생성하지 않고 예외를 반환한다.")
    void should_throw_exception_when_nickname_null() {

        //given
        String email = "test@naver.com";
        String password = "password";
        String nickname = null;
        String profilePath = "/image_profile/";

        //when & then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(email, password, nickname, profilePath);
        });
    }

    @Test
    @DisplayName("profilePath가 null이라면 User 객체를 새로 생성하지 않고 예외를 반환한다.")
    void should_throw_exception_when_profilePath_null() {

        //given
        String email = "test@naver.com";
        String password = "password";
        String nickname = "test";
        String profilePath = null;

        //when & then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(email, password, nickname, profilePath);
        });
    }

    @Test
    @DisplayName("User 객체를 전달하면 UserEntity형으로 반환돼야 한다.")
    void should_return_entity_when_user_get() {

        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        UserEntity userEntity = User.toEntity(user);

        //then
        assertEquals(user.getId(), userEntity.getId());
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(user.getNickname(), userEntity.getNickname());
        assertEquals(user.getPassword(), userEntity.getPassword());
        assertEquals(user.getProfilePath(), userEntity.getProfilePath());
        assertEquals(user.getIsActive(), userEntity.getIsActive());
        assertEquals(user.getCreatedAt(), userEntity.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userEntity.getUpdatedAt());

    }

    @Test
    @DisplayName("User 객체의 값 중 email이 변경되면 새로운 User 객체가 생성된다.")
    void should_return_new_user_when_change_email() {
        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        User newUser = user.withEmail("newTest@naver.com");

        //then
        assertNotEquals(user.getEmail(), newUser.getEmail());
        assertEquals("newTest@naver.com", newUser.getEmail());
        assertEquals(user.getNickname(), newUser.getNickname());
        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getProfilePath(), newUser.getProfilePath());
        assertEquals(user.getIsActive(), newUser.getIsActive());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getCreatedAt(), newUser.getCreatedAt());
        assertEquals(user.getUpdatedAt(), newUser.getUpdatedAt());

    }

    @Test
    @DisplayName("User 객체의 값 중 nickname이 변경되면 새로운 User 객체가 생성된다.")
    void should_return_new_user_when_change_nickname() {
        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        User newUser = user.withNickname("newTest");

        //then
        assertNotEquals(user.getNickname(), newUser.getNickname());
        assertEquals("newTest", newUser.getNickname());
        assertEquals(user.getEmail(), newUser.getEmail());
        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getProfilePath(), newUser.getProfilePath());
        assertEquals(user.getIsActive(), newUser.getIsActive());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getCreatedAt(), newUser.getCreatedAt());
        assertEquals(user.getUpdatedAt(), newUser.getUpdatedAt());

    }

    @Test
    @DisplayName("User 객체의 값 중 email이 변경되면 새로운 User 객체가 생성된다.")
    void should_return_new_user_when_change_password() {
        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        User newUser = user.withEmail("newTest@naver.com");

        //then
        assertNotEquals(user.getEmail(), newUser.getEmail());
        assertEquals("newTest@naver.com", newUser.getEmail());
        assertEquals(user.getNickname(), newUser.getNickname());
        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getProfilePath(), newUser.getProfilePath());
        assertEquals(user.getIsActive(), newUser.getIsActive());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getCreatedAt(), newUser.getCreatedAt());
        assertEquals(user.getUpdatedAt(), newUser.getUpdatedAt());

    }

    @Test
    @DisplayName("User 객체의 값 중 email이 변경되면 새로운 User 객체가 생성된다.")
    void should_return_new_user_when_change_profilePath() {
        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        User newUser = user.withEmail("newTest@naver.com");

        //then
        assertNotEquals(user.getEmail(), newUser.getEmail());
        assertEquals("newTest@naver.com", newUser.getEmail());
        assertEquals(user.getNickname(), newUser.getNickname());
        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getProfilePath(), newUser.getProfilePath());
        assertEquals(user.getIsActive(), newUser.getIsActive());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getCreatedAt(), newUser.getCreatedAt());
        assertEquals(user.getUpdatedAt(), newUser.getUpdatedAt());

    }

    @Test
    @DisplayName("User 객체의 값 중 email이 변경되면 새로운 User 객체가 생성된다.")
    void should_return_new_user_when_change_active() {
        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        User newUser = user.withEmail("newTest@naver.com");

        //then
        assertNotEquals(user.getEmail(), newUser.getEmail());
        assertEquals("newTest@naver.com", newUser.getEmail());
        assertEquals(user.getNickname(), newUser.getNickname());
        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getProfilePath(), newUser.getProfilePath());
        assertEquals(user.getIsActive(), newUser.getIsActive());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getCreatedAt(), newUser.getCreatedAt());
        assertEquals(user.getUpdatedAt(), newUser.getUpdatedAt());

    }

    @Test
    @DisplayName("User 객체의 값 중 email이 변경되면 새로운 User 객체가 생성된다.")
    void should_return_new_user_when_change_created() {
        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        User newUser = user.withEmail("newTest@naver.com");

        //then
        assertNotEquals(user.getEmail(), newUser.getEmail());
        assertEquals("newTest@naver.com", newUser.getEmail());
        assertEquals(user.getNickname(), newUser.getNickname());
        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getProfilePath(), newUser.getProfilePath());
        assertEquals(user.getIsActive(), newUser.getIsActive());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getCreatedAt(), newUser.getCreatedAt());
        assertEquals(user.getUpdatedAt(), newUser.getUpdatedAt());

    }

    @Test
    @DisplayName("User 객체의 값 중 email이 변경되면 새로운 User 객체가 생성된다.")
    void should_return_new_user_when_change_updated() {
        //given
        User user = User.createUser("test@naver.com", "password", "test", "/image_profile/");

        //when
        User newUser = user.withEmail("newTest@naver.com");

        //then
        assertNotEquals(user.getEmail(), newUser.getEmail());
        assertEquals("newTest@naver.com", newUser.getEmail());
        assertEquals(user.getNickname(), newUser.getNickname());
        assertEquals(user.getId(), newUser.getId());
        assertEquals(user.getProfilePath(), newUser.getProfilePath());
        assertEquals(user.getIsActive(), newUser.getIsActive());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getCreatedAt(), newUser.getCreatedAt());
        assertEquals(user.getUpdatedAt(), newUser.getUpdatedAt());

    }

}