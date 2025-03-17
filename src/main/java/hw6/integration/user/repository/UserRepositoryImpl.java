package hw6.integration.user.repository;

import hw6.integration.user.entity.UserEntity;
import hw6.integration.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<List<User>> findByAll() {

        return Optional.of(
                userJpaRepository.findAll().stream()
                        .map(UserEntity::toDomain)
                        .toList()
        );

    }

    @Override
    public Optional<User> findById(Long id) {

        return userJpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }


    @Override
    public User save(User user) {
        UserEntity userEntity = userJpaRepository.save(User.toEntity(user));
        return userEntity.toDomain();
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(User.toEntity(user));
    }
}
