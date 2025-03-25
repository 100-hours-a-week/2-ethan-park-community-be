package hw6.integration.user.repository;

import hw6.integration.user.domain.User;
import hw6.integration.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserWriterRepositoryImpl implements UserWriterRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = userJpaRepository.save(User.toEntity(user));
        return userEntity.toDomain();
    }
}
