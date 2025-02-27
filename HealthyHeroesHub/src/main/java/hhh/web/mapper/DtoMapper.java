package hhh.web.mapper;
import hhh.user.model.User;
import hhh.web.dto.EditProfileRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {
    public static EditProfileRequest toEditProfileRequest(User user) {
        return EditProfileRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .phoneNumber(user.getPhone())
                .build();
    }
}
