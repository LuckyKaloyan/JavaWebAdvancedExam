package hhh.user.service;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.repository.UserRepository;
import hhh.web.dto.EditProfileRequest;
import hhh.web.dto.LoginRequest;
import hhh.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void register(RegisterRequest registerRequest) {

        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username %s already exist".formatted(registerRequest.getUsername()));
        }
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email %s already exists".formatted(registerRequest.getEmail()));
        }

        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .registrationDate(LocalDate.now())
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->new RuntimeException("User not found"));

        return new AuthenticationDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }
    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public void editUser(UUID userId, EditProfileRequest editProfileRequest) {
        User user = getById(userId);
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setEmail(editProfileRequest.getEmail());
        user.setProfilePicture(editProfileRequest.getProfilePicture());
        user.setPhone(editProfileRequest.getPhoneNumber());
        userRepository.save(user);
    }

}