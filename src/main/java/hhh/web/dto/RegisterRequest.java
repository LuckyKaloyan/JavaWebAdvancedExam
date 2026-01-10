package hhh.web.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @Size(min = 2, max = 15, message = "Enter an username between 2 and 15 symbols! Please!")
    private String username;

    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String password;

    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String confirmPassword;

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]{2,64}@[A-Za-z0-9.-]+\\.[a-z]{1,10}$",
            message = "Please enter a valid Email address!"
    )
    private String email;

    // No-args constructor
    public RegisterRequest() {
    }

    // All-args constructor
    public RegisterRequest(
            String username,
            String password,
            String confirmPassword,
            String email
    ) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
