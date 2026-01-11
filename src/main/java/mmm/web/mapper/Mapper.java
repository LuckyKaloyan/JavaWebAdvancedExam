package mmm.web.mapper;

import mmm.eatenmealslist.model.EatenMealsList;
import mmm.mealcatalog.model.MealCatalog;
import mmm.user.model.User;
import mmm.web.dto.EatenMealsRequest;
import mmm.web.dto.EditCatalogRequest;
import mmm.web.dto.EditProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public static EditProfileRequest toEditProfileRequest(User user) {
        EditProfileRequest dto = new EditProfileRequest();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setPhoneNumber(user.getPhone());
        return dto;
    }

    public static EditCatalogRequest toEditCatalogRequest(MealCatalog mealCatalog) {
        EditCatalogRequest dto = new EditCatalogRequest();
        dto.setName(mealCatalog.getName());
        dto.setDescription(mealCatalog.getDescription());
        return dto;
    }

    public EatenMealsRequest toRequest(EatenMealsList eatenMealsList) {
        EatenMealsRequest dto = new EatenMealsRequest();
        dto.setUserId(eatenMealsList.getUserId());
        dto.setMealsIds(eatenMealsList.getMealsIds());
        return dto;
    }

    public EatenMealsList toEntity(EatenMealsRequest eatenMealsListDto) {
        EatenMealsList entity = new EatenMealsList();
        entity.setUserId(eatenMealsListDto.getUserId());
        entity.setMealsIds(eatenMealsListDto.getMealsIds());
        return entity;
    }
}
