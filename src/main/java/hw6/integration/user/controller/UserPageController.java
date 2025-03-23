package hw6.integration.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup/signup";
    }

    @GetMapping("/edit-password")
    public String editPasswordPage() {
        return "edit-password/edit-password";
    }

    @GetMapping("/edit-profile")
    public String editProfilePage() {
        return "edit-profile/edit-profile";
    }
}
