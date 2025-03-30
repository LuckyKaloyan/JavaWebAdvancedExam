package hhh.web.dto;
import hhh.report.model.ReportType;
import hhh.report.model.ReportLocation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {


    @NotNull(message = "This field cannot be empty!")
    private String troublemaker;
    @NotNull(message = "This field cannot be empty!")
    private ReportType reportType;
    @NotNull(message = "This field cannot be empty!")
    private ReportLocation whereItHappened;
    @NotNull(message = "This field cannot be empty!")
    private String description;
    @NotNull(message = "Please select a date")
    @PastOrPresent(message = "Date must be today or earlier")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfIssue;

}

