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
import java.util.List;
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

    public void createDefaultAdmin(){
        User user = User.builder()
                .password(passwordEncoder.encode("123123"))
                .username("luckykaloyan")
                .role(UserRole.ADMIN)
                .email("luckykaloyan@gmail.com")
                .registrationDate(LocalDate.now())
                .dailyCalories(0)
                .build();
        userRepository.save(user);
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
                .dailyCalories(0)
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
    public void deleteUser(UUID userId) {
        User user = getById(userId);
        userRepository.delete(user);
    }
    public List<User> getAll() {
        return userRepository.findAll();
    }
    public List<User> getAllByRole(UserRole role) {
        return userRepository.findByRole(role);
    }
    public List<User> getAllRegisteredLastMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        return userRepository.findByRegistrationDateAfter(oneMonthAgo);
    }
    public List<User> getAllRegisteredLastYear() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return userRepository.findByRegistrationDateAfter(oneYearAgo);
    }
    public List<User> getAllRegisteredLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        return userRepository.findByRegistrationDateAfter(oneWeekAgo);
    }
    public List<User> getAllRegisteredLast24hours() {
        LocalDate oneDayAgo = LocalDate.now().minusDays(1);
        return userRepository.findByRegistrationDateAfter(oneDayAgo);
    }


    @Transactional
    public void changeRole(UUID id) {
        User user = getById(id);
        if (user.getRole() == UserRole.ADMIN) {
            user.setRole(UserRole.USER);
        } else {
            user.setRole(UserRole.ADMIN);
        }
        userRepository.save(user);
    }

    public void setDailyCalories(UUID id, double dailyCalories) {
        User user = getById(id);
        user.setDailyCalories(dailyCalories);
        userRepository.save(user);
    }

}