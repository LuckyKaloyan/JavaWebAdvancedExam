package hhh.web.mapper;
import hhh.mealcatalog.model.MealCatalog;
import hhh.user.model.User;
import hhh.web.dto.EditCatalogRequest;
import hhh.web.dto.EditProfileRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapperUnitTest {

    @Test
    void toEditProfileRequest_shouldMapUserCorrectly() {
        User user = new User();
        user.setFirstName("Lucky");
        user.setLastName("Kaloyan");
        user.setEmail("luckykaloyan@gmail.com");
        user.setProfilePicture("japeg");
        user.setPhone("0896811625");
        EditProfileRequest result = Mapper.toEditProfileRequest(user);
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getProfilePicture(), result.getProfilePicture());
        assertEquals(user.getPhone(), result.getPhoneNumber());
    }

    @Test
    void toEditCatalogRequest_shouldMapMealCatalogCorrectly() {
        MealCatalog catalog = new MealCatalog();
        catalog.setName("Catalog");
        catalog.setDescription("Description");
        EditCatalogRequest result = Mapper.toEditCatalogRequest(catalog);
        assertEquals(catalog.getName(), result.getName());
        assertEquals(catalog.getDescription(), result.getDescription());
    }

}