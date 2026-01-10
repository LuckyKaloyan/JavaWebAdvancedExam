package hhh.web.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class EditProfileRequest {

    @Size(min = 2, max = 20, message = "First name length must be between 2 and 20 characters (including 2 and 20).")
    @Pattern(
            regexp = "^[A-Z][a-z]+$",
            message = "First name must start with a capital letter and contain only latin letters."
    )
    private String firstName;

    @Size(min = 2, max = 20, message = "Last name length must be between 2 and 20 characters (including 2 and 20).")
    @Pattern(
            regexp = "^[A-Z][a-z]+$",
            message = "Last name must start with a capital letter and contain only latin letters."
    )
    private String lastName;

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]{2,64}@[A-Za-z0-9.-]+\\.[a-z]{1,10}$",
            message = "Please enter a valid Email address!"
    )
    private String email;

    @URL(message = "Must contain a valid URL.")
    private String profilePicture;

    @Size(min = 9, max = 20, message = "Add a valid phone number please")
    private String phoneNumber;

    // No-args constructor
    public EditProfileRequest() {
    }

    // All-args constructor
    public EditProfileRequest(
            String firstName,
            String lastName,
            String email,
            String profilePicture,
            String phoneNumber
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
