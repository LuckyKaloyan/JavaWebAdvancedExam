package mmm.web.dto;

import mmm.mealcatalog.model.MealCatalogType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MealCatalogRequest {

    @NotNull
    @Size(min = 2, max = 30, message = "Enter a catalog name between 2 and 30 symbols! Please!")
    private String name;

    @NotNull
    @Size(min = 2, max = 300, message = "Enter a description between 2 and 300 symbols! Please!")
    private String description;

    @NotNull
    private MealCatalogType type;

    // No-args constructor
    public MealCatalogRequest() {
    }

    // All-args constructor
    public MealCatalogRequest(String name, String description, MealCatalogType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MealCatalogType getType() {
        return type;
    }

    public void setType(MealCatalogType type) {
        this.type = type;
    }
}
