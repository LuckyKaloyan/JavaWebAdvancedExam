package hhh.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCatalogRequest {

    @NotNull
    @Size(min = 2, max = 15, message = "Enter a catalog name between 2 and 15 symbols! Please!")
    private String name;

    @NotNull
    @Size(min = 2, max = 300, message = "Enter a description between 2 and 300 symbols! Please!")
    private String description;

}
