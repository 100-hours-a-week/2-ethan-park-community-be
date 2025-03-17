package hw6.integration.exception;

import jakarta.persistence.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {

    public PostNotFoundException() {
        super("게시글이 없습니다.");
    }

    public PostNotFoundException(Long id) {
        super(id + "번 게시글을 찾을 수 없습니다.");
    }
}