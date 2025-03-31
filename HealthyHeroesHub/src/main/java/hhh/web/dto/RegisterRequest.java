package hhh.web.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 2, max = 15, message = "Enter an username between 2 and 15 symbols! Please!")
    private String username;

    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String password;

    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String confirmPassword;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]{2,64}@[A-Za-z0-9.-]+\\.[a-z]{1,10}$", message = "Please enter a valid Email address!")
    private String email;


}
