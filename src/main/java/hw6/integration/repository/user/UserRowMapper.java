package hw6.integration.repository.user;

import hw6.integration.domain.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//        User user = new User();
//        user.setUserId(rs.getLong("user_id"));
//        user.setEmail(rs.getString("email"));
//        user.setPassword(rs.getString("password"));
//        user.setNickname(rs.getString("nickname"));
//        user.setProfilePath(rs.getString("profile_path"));
//        user.setCreatedAt(rs.getTimestamp("created_at"));
//        user.setActive(rs.getBoolean("active"));
//        return user;
        return null;
    }
}
