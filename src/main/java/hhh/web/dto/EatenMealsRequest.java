package hhh.web.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EatenMealsRequest {
    private UUID userId;
    private List<UUID> mealsIds = new ArrayList<>();
}