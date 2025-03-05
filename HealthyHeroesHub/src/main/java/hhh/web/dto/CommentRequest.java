package hhh.web.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @Size(min = 2, max = 500, message = "Come clean! Use between 2 and 500 letters.")
    private String text;
}
