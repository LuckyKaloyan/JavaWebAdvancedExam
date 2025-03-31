package hhh.web.dto;
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
public class LoginRequest {

    @NotNull
    @Size(min = 2, max = 15, message = "Enter an username between 2 and 15 symbols! Please!")
    private String username;

    @NotNull
    @Size(min = 2, max = 15, message = "Enter a password between 2 and 15 symbols! Please!")
    private String password;
}
