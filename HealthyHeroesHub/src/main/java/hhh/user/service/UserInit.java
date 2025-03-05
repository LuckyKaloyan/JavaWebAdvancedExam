package hhh.user.service;

import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserInit implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public UserInit(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userService.getAll().isEmpty()){
             userService.createDefaultAdmin();
        }
    }
}
