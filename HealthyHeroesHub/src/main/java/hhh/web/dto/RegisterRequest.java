package hhh.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @Size(min = 2, max = 15, message = "Enter an username between 2 and 15 symbols! Please!")
    private String username;

    @NotNull
    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String password;

    @NotNull
    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String confirmPassword;

    @Email(message = "Enter a valid email please!")
    @NotNull
    private String email;


}
