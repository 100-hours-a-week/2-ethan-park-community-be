package hw6.integration.repository.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private CommentJpaRepository commentJpaRepository;
}
