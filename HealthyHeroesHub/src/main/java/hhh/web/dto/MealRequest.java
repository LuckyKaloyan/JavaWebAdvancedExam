package hhh.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
