package hhh.event;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealOfTheDayEvent {

    @NotNull
    private int upVotes;
    @NotBlank
    private String name;
    @NotBlank
    private String ownersName;
    @NotBlank
    private String description;


}
