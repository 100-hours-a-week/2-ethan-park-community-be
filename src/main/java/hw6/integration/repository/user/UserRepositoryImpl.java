package hw6.integration.repository.user;

import hw6.integration.domain.entity.UserEntity;
import hw6.integration.domain.model.User;
import jakarta.persistence.EntityNotFoundException;
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
}
