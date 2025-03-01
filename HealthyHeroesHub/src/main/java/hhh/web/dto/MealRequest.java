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
    @Size(min = 2, max = 50)
    private String name;
    @NotNull
    @Size(min = 20, max = 100)
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal proteins;
    @NotNull
    @PositiveOrZero
    private BigDecimal carbs;
    @NotNull
    @PositiveOrZero
    private BigDecimal fats;
    @NotNull
    @URL
    private String picture;
}
