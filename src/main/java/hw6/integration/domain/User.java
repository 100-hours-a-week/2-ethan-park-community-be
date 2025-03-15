package hw6.integration.domain;

import java.sql.Timestamp;

public class User {

    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String profilePath;
    private Timestamp createdAt;
    private Boolean active;

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
