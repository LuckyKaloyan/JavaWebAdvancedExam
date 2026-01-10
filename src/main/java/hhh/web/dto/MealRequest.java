package hhh.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public class MealRequest {

    @NotNull
    @Size(min = 2, max = 50, message = "Enter a name between 2 and 50 symbols.")
    private String name;

    @NotNull
    @Size(min = 2, max = 200, message = "Enter a description between 2 and 200 symbols.")
    private String description;

    @NotNull
    @PositiveOrZero(message = "The field cannot be negative!")
    private BigDecimal proteins;

    @NotNull
    @PositiveOrZero(message = "The field cannot be negative!")
    private BigDecimal carbs;

    @NotNull
    @PositiveOrZero(message = "The field cannot be negative!")
    private BigDecimal fats;

    @NotNull
    @URL
    @Size(max = 1000, message = "Please choose a smaller picture!")
    private String picture;

    // No-args constructor
    public MealRequest() {
    }

    // All-args constructor
    public MealRequest(
            String name,
            String description,
            BigDecimal proteins,
            BigDecimal carbs,
            BigDecimal fats,
            String picture
    ) {
        this.name = name;
        this.description = description;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fats = fats;
        this.picture = picture;
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

    public BigDecimal getProteins() {
        return proteins;
    }

    public void setProteins(BigDecimal proteins) {
        this.proteins = proteins;
    }

    public BigDecimal getCarbs() {
        return carbs;
    }

    public void setCarbs(BigDecimal carbs) {
        this.carbs = carbs;
    }

    public BigDecimal getFats() {
        return fats;
    }

    public void setFats(BigDecimal fats) {
        this.fats = fats;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
