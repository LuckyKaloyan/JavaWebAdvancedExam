package hhh.web.mapper;
import hhh.meal.model.Meal;
import hhh.meal.model.MealOfTheHour;
import hhh.mealcatalog.model.MealCatalog;
import hhh.user.model.User;
import hhh.web.dto.EditCatalogRequest;
import hhh.web.dto.EditProfileRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Mapper {

    public static EditProfileRequest toEditProfileRequest(User user) {
        return EditProfileRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .phoneNumber(user.getPhone())
                .build();
    }
    public static EditCatalogRequest toEditCatalogRequest(MealCatalog mealCatalog) {
        return EditCatalogRequest.builder()
                .name(mealCatalog.getName())
                .description(mealCatalog.getDescription())
                .build();
    }
    public static MealOfTheHour toMealOfTheDay(Meal meal) {
        return MealOfTheHour.builder()
                .name(meal.getName())
                .description(meal.getDescription())
                .mealId(meal.getId())
                .upVotes(meal.getUpVotes().size())
                .build();
    }
}
