package mmm.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotNull
    @Size(min = 2, max = 15, message = "Enter an username between 2 and 15 symbols! Please!")
    private String username;

    @NotNull
    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String password;

    // No-args constructor
    public LoginRequest() {
    }

    // All-args constructor
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
}
