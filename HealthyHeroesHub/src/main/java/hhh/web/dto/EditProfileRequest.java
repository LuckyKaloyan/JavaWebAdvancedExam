package hhh.web.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditProfileRequest {


    @Size(min = 2, max = 20, message = "First name length must be between 2 and 20 characters (including 2 and 20).")
    private String firstName;

    @Size(min = 2, max = 20, message = "Last name length must be between 2 and 20 characters (including 2 and 20).")
    private String lastName;


    @Email(message = "email address containing @.")
    private String email;

    @URL(message = "Must contain a valid URL.")
    private String profilePicture;

    @Size(min = 9, max = 20, message = "Add a valid phone number please")
    private String phoneNumber;
}
