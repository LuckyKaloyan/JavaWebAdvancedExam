package mmm.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditCatalogRequest {

    @NotNull
    @Size(min = 2, max = 15, message = "Enter a catalog name between 2 and 15 symbols! Please!")
    private String name;

    @NotNull
    @Size(min = 2, max = 300, message = "Enter a description between 2 and 300 symbols! Please!")
    private String description;

    // No-args constructor
    public EditCatalogRequest() {
    }

    // All-args constructor
    public EditCatalogRequest(String name, String description) {
        this.name = name;
        this.description = description;
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
}
