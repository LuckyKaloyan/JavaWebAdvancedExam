package hhh.web.dto;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "First name must start with a capital letter and contain only latin letters.")
    private String firstName;

    @Size(min = 2, max = 20, message = "Last name length must be between 2 and 20 characters (including 2 and 20).")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "Last name must start with a capital letter and contain only latin letters.")
    private String lastName;


    @Pattern(regexp = "^[A-Za-z0-9._%+-]{2,64}@[A-Za-z0-9.-]+\\.[a-z]{1,10}$", message = "Please enter a valid Email address!")
    private String email;

    @URL(message = "Must contain a valid URL.")
    private String profilePicture;

    @Size(min = 9, max = 20, message = "Add a valid phone number please")
    private String phoneNumber;
}
