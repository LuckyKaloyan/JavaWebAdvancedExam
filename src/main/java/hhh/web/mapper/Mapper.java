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
