package hw6.integration.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class User {

    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String profilePath;

    private Boolean is_active;

    private Timestamp createdAt;

    public User(Long id, String email, String password, String nickname, String profilePath,
                Boolean is_active, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.createdAt = createdAt;
        this.is_active = is_active;
    }

}
