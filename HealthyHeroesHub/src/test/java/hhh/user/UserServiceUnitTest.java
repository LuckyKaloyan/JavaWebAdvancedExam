package hhh.user;
import hhh.exception.BadInputException;
import hhh.exception.EmailAlreadyInUseException;
import hhh.exception.PasswordsDoNotMatchException;
import hhh.exception.UsernameAlreadyExistException;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.repository.UserRepository;
import hhh.user.service.UserService;
import hhh.web.dto.EditProfileRequest;
import hhh.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterRequest registerRequest;
    private User testUser;
    private UUID testUserId;

    @InjectMocks
    private UserService userService;
    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "luckykaloyan",
                "123123",
                "123123",
                "luckykaloyan@gmail.com"
        );
        testUserId = UUID.randomUUID();
        testUser = User.builder()
                .id(testUserId)
                .username("luckykaloyan")
                .password("123123")
                .email("luckykaloyan@gmail.com")
                .role(UserRole.USER)
                .dailyCalories(2000)
                .build();
    }
        @Test
        void registerWithValidRequestSaveUser () {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            userService.register(registerRequest);
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        void registerWithExistingUsernameThrowException () {
            when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new User()));
            assertThrows(UsernameAlreadyExistException.class, () -> userService.register(registerRequest));
        }

        @Test
        void registerWithExistingEmailThrowException () {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));
            assertThrows(EmailAlreadyInUseException.class, () -> userService.register(registerRequest));
        }

        @Test
        void registerWithPasswordMismatchThrowException () {
            RegisterRequest invalidRequest = new RegisterRequest(
                    "luckykaloyan",
                    "123123",
                    "456456",
                    "luckykaloyan@luckykaloyan.com"
            );
            assertThrows(PasswordsDoNotMatchException.class, () -> userService.register(invalidRequest));
        }

       @Test
       void registerSaveUser() {
           when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
           when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
           when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
           userService.register(registerRequest);
           verify(userRepository).save(any(User.class));
    }
       @Test
       public void testCreateDefaultAdmin() {
           User admin = new User();
           admin.setUsername("luckykaloyan");
           when(userRepository.findByUsername("luckykaloyan")).thenReturn(Optional.of(admin));
           userService.createDefaultAdmin();
           Optional<User> optionalUser = userRepository.findByUsername("luckykaloyan");
           assertTrue(optionalUser.isPresent());
           User luckyKaloyan = optionalUser.get();
           assertEquals("luckykaloyan", luckyKaloyan.getUsername());
    }

    @Test
    void loadUserByUsernameReturnUserDetails() {
        when(userRepository.findByUsername("luckykaloyan")).thenReturn(Optional.of(testUser));
        UserDetails result = userService.loadUserByUsername("luckykaloyan");
        assertNotNull(result);
        assertEquals("luckykaloyan", result.getUsername());
        verify(userRepository).findByUsername("luckykaloyan");
    }

    @Test
    void loadUserByUsernameWithNullUsernameThrowException() {
        assertThrows(BadInputException.class, () -> userService.loadUserByUsername(null));
    }

    @Test
    void loadUserByUsernameWithEmptyUsernameThrowException() {
        assertThrows(BadInputException.class, () -> userService.loadUserByUsername(""));
    }

    @Test
    void loadUserByUsernameWithNonExistentUserThrowException() {
        when(userRepository.findByUsername("luckykaloyan")).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> userService.loadUserByUsername("luckykaloyan"));
    }

    @Test
    void getById_ShouldReturnUser() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        User result = userService.getById(testUserId);
        assertEquals(testUser, result);
        verify(userRepository).findById(testUserId);
    }

    @Test
    void getById_WithNullIdThrowException() {
        assertThrows(BadInputException.class, () -> userService.getById(null));
    }

    @Test
    void getByIdWithNonExistentIdThrowException() {
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> userService.getById(nonExistentId));
    }
    @Test
    void editUserUpdateUser() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        EditProfileRequest request = new EditProfileRequest(
                 "Lucky",
                "Kaloyan",
                "luckykaloyan@email.com",
                "profilnaFacebook.jpg",
                "0896811625");

        userService.editUser(testUserId, request);
        assertEquals("Lucky", testUser.getFirstName());
        assertEquals("Kaloyan", testUser.getLastName());
        assertEquals("luckykaloyan@email.com", testUser.getEmail());
        verify(userRepository).save(testUser);
    }
    @Test
    void editUserWithNullIdThrowException() {
        assertThrows(BadInputException.class, () -> userService.editUser(null, new EditProfileRequest()));
    }

    @Test
    void editUserWithNullRequestThrowException() {
        assertThrows(BadInputException.class, () -> userService.editUser(testUserId, null));
    }
    @Test
    void deleteUserDeleteUser() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        userService.deleteUser(testUserId);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUserWithNullIdThrowException() {
        assertThrows(BadInputException.class, () -> userService.deleteUser(null));
    }

    @Test
    void getAllReturnAllUsers() {
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAll();
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void getAllByRoleReturnUsersWithRole() {
        when(userRepository.findByRole(UserRole.USER)).thenReturn(List.of(testUser));
        List<User> result = userService.getAllByRole(UserRole.USER);
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void getAllByRoleWithNullRoleThrowException() {
        assertThrows(BadInputException.class, () -> userService.getAllByRole(null));
    }

    @Test
    void changeRoleToggleRole() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        userService.changeRole(testUserId);
        assertEquals(UserRole.ADMIN, testUser.getRole());
        userService.changeRole(testUserId);
        assertEquals(UserRole.USER, testUser.getRole());
        verify(userRepository, times(2)).save(testUser);
    }

    @Test
    void changeRoleWithNullIdThrowException() {
        assertThrows(BadInputException.class, () -> userService.changeRole(null));
    }


    @Test
    void setDailyCaloriesUpdateCalories() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        userService.setDailyCalories(testUserId, 2500);
        assertEquals(2500, testUser.getDailyCalories());
        verify(userRepository).save(testUser);
    }

    @Test
    void setDailyCalories_WithNegativeValueThrowException() {
        assertThrows(BadInputException.class, () -> userService.setDailyCalories(testUserId, -100));
    }


    @Test
    void increaseDailyCalories500Add500() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        double initialCalories = testUser.getDailyCalories();
        userService.increaseDailyCalories500(testUserId);
        assertEquals(initialCalories + 500, testUser.getDailyCalories());
        verify(userRepository).save(testUser);
    }


    @Test
    void decreaseDailyCalories500Subtract500() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        double initialCalories = testUser.getDailyCalories();
        userService.decreaseDailyCalories500(testUserId);
        assertEquals(initialCalories - 500, testUser.getDailyCalories());
        verify(userRepository).save(testUser);
    }

    @Test
    void decreaseDailyCalories500NotGoBelowZero() {
        testUser.setDailyCalories(300);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        userService.decreaseDailyCalories500(testUserId);
        assertEquals(0, testUser.getDailyCalories());
        verify(userRepository).save(testUser);
    }

    @Test
    void getAllRegisteredLastMonthReturnUsers() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        when(userRepository.findByRegistrationDateAfter(oneMonthAgo)).thenReturn(List.of(testUser));
        List<User> result = userService.getAllRegisteredLastMonth();
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void getAllRegisteredLastWeekReturnUsers() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        when(userRepository.findByRegistrationDateAfter(oneWeekAgo)).thenReturn(List.of(testUser));
        List<User> result = userService.getAllRegisteredLastWeek();
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void getAllRegisteredLastYearReturnUsers() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        when(userRepository.findByRegistrationDateAfter(oneYearAgo)).thenReturn(List.of(testUser));
        List<User> result = userService.getAllRegisteredLastYear();
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void getAllRegisteredLastDayReturnUsers() {
        LocalDate oneDayAgo = LocalDate.now().minusDays(1);
        when(userRepository.findByRegistrationDateAfter(oneDayAgo)).thenReturn(List.of(testUser));
        List<User> result = userService.getAllRegisteredLast24hours();
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void nullCalculatorExceptionsTest() {
        assertThrows(BadInputException.class, () -> {userService.decreaseDailyCalories500(null);});
        assertThrows(BadInputException.class, () -> {userService.increaseDailyCalories500(null);});
        assertThrows(BadInputException.class, () -> {userService.setDailyCalories(null, 100);});
    }

    }