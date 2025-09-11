package hhh.web.mapper;
import hhh.eatenmealslist.model.EatenMealsList;
import hhh.mealcatalog.model.MealCatalog;
import hhh.user.model.User;
import hhh.web.dto.EatenMealsRequest;
import hhh.web.dto.EditCatalogRequest;
import hhh.web.dto.EditProfileRequest;
import org.springframework.stereotype.Component;


@Component
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
    public EatenMealsRequest toRequest(EatenMealsList eatenMealsList) {
        return EatenMealsRequest.builder()
                .userId(eatenMealsList.getUserId())
                .mealsIds(eatenMealsList.getMealsIds())
                .build();
    }
    public EatenMealsList toEntity(EatenMealsRequest eatenMealsListDto) {
        return EatenMealsList.builder()
                .userId(eatenMealsListDto.getUserId())
                .mealsIds(eatenMealsListDto.getMealsIds())
                .build();
    }
}
