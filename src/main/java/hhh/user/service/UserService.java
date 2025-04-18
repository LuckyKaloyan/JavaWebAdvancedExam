package hhh.user.service;
import hhh.exception.BadInputException;
import hhh.exception.EmailAlreadyInUseException;
import hhh.exception.PasswordsDoNotMatchException;
import hhh.exception.UsernameAlreadyExistException;
import hhh.meal.model.Meal;
import hhh.security.AuthenticationDetails;
import hhh.upvote.model.UpVote;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.repository.UserRepository;
import hhh.web.dto.EditProfileRequest;
import hhh.web.dto.RegisterRequest;
import hhh.winner.model.Winner;
import hhh.winner.service.WinnerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WinnerService winnerService;

    @Autowired
    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder, @Lazy WinnerService winnerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.winnerService = winnerService;
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
            throw new UsernameAlreadyExistException("Username %s already exist!".formatted(registerRequest.getUsername()));
        }
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email %s in use!".formatted(registerRequest.getEmail()));
        }

        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
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


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new BadInputException("Username cannot be null or empty");
        }

        User user = userRepository.findByUsername(username).orElseThrow(() ->new BadInputException("User not found"));
        return new AuthenticationDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }
    public User getById(UUID id) {
        if (id == null) {
            throw new BadInputException("User ID cannot be null");
        }
        return userRepository.findById(id).orElseThrow(() -> new BadInputException("User not found!"));
    }

    public void editUser(UUID userId, EditProfileRequest editProfileRequest) {
        if (userId == null) {
            throw new BadInputException("User ID cannot be null");
        }
        if (editProfileRequest == null) {
            throw new BadInputException("EditProfileRequest cannot be null");
        }
        User user = getById(userId);
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setEmail(editProfileRequest.getEmail());
        user.setProfilePicture(editProfileRequest.getProfilePicture());
        user.setPhone(editProfileRequest.getPhoneNumber());
        userRepository.save(user);
    }
  
    public List<User> getAll() {
        return userRepository.findAll();
    }
    public List<User> getAllByRole(UserRole role) {
        if (role == null) {
            throw new BadInputException("Role cannot be null");
        }
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

     public List<Meal> getAllMeals(User user) {
        List<Meal> meals = new ArrayList<>();
        user.getMealCatalogs().forEach(mealCatalog -> {meals.addAll(mealCatalog.getMeals());});
        return meals;
     }
     public List<UpVote> getAllUpVotesToUsersMeals(User user) {
        List<UpVote> upVotes = new ArrayList<>();
        user.getMealCatalogs().forEach(mealCatalog ->
        {mealCatalog.getMeals().forEach(meal -> meal.getUpVotes().forEach(upVote -> upVotes.add(upVote)));});
                return upVotes;
     }

    @Transactional
    public void changeRole(UUID id) {
        if (id == null) {
            throw new BadInputException("User ID cannot be null");
        }
        User user = getById(id);
        if (user.getRole() == UserRole.ADMIN) {
            user.setRole(UserRole.USER);
        } else {
            user.setRole(UserRole.ADMIN);
        }
        userRepository.save(user);
    }

    public void setDailyCalories(UUID id, double dailyCalories) {
        if (id == null) {
            throw new BadInputException("User ID cannot be null");
        }
        if (dailyCalories < 0) {
            throw new BadInputException("Daily calories cannot be negative");
        }
        User user = getById(id);
        user.setDailyCalories(dailyCalories);
        userRepository.save(user);
    }
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->new BadInputException("User not found"));
    }

    public void increaseDailyCalories500(UUID id) {
        if (id == null) {
            throw new BadInputException("User ID cannot be null");
        }
        User user = getById(id);
        user.setDailyCalories(user.getDailyCalories() + 500);
        userRepository.save(user);
    }
    public void decreaseDailyCalories500(UUID id) {
        if (id == null) {
            throw new BadInputException("User ID cannot be null");
        }
        User user = getById(id);
        user.setDailyCalories(user.getDailyCalories() - 500);
        if(user.getDailyCalories() < 0) {
            user.setDailyCalories(0);
        }
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.saveAllAndFlush(userRepository.findAll());

        user.getMealCatalogs().forEach(mealCatalog -> {mealCatalog.getMeals().forEach(meal -> {meal.setMealCatalog(null);meal.setOwner(null);});mealCatalog.setOwner(null);});
        user.getMealCatalogs().clear();
        user.getFavouriteMeals().clear();
        user.getUpVotes().clear();
        user.getComments().forEach(comment -> comment.setUser(null));
        user.getComments().clear();
        user.getReports().forEach(report -> report.setConcernedUser(null));
        user.getReports().clear();
        if (user.getWinner() != null) {
            Winner winner = user.getWinner();
            winner.setUser(null);
            winner.setMeal(null);
            winnerService.saveIt(winner);
        }

        userRepository.delete(user);
    }
}