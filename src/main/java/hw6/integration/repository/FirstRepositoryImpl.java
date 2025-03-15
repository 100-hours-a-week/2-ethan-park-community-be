package hw6.integration.repository;

import hw6.integration.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


@Repository
public class FirstRepositoryImpl implements FirstRepository{

    private final JdbcTemplate jdbcTemplate;

    public FirstRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findTestValue() {

        String sql = "SELECT * FROM USER";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        return users;
    }
}
