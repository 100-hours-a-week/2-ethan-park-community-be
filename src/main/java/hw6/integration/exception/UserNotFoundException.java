package hw6.integration.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super("유저가 없습니다.");
    }

    public UserNotFoundException(Long id) {
        super(id + "번 유저를 찾을 수 없습니다.");
    }
}
