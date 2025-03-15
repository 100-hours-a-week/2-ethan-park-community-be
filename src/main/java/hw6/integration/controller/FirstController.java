package hw6.integration.controller;

import hw6.integration.domain.User;
import hw6.integration.service.FirstService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FirstController {

    private final FirstService firstService;

    public FirstController(FirstService firstService) {
        this.firstService = firstService;
    }

    @GetMapping("/user")
    public List<User> test() {

        return firstService.test();
    }
}
