package hw6.integration.service;

import hw6.integration.domain.User;
import hw6.integration.repository.FirstRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirstServiceImpl implements FirstService{

    private final FirstRepository firstRepository;

    public FirstServiceImpl(FirstRepository firstRepository) {
        this.firstRepository = firstRepository;
    }

    @Override
    public List<User> test() {

        return firstRepository.findTestValue();
    }
}
