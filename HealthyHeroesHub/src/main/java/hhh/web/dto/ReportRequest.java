package hhh.web.dto;
import hhh.report.model.ReportType;
import hhh.report.model.ReportLocation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
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
    @NotNull(message = "You have to choose a date!")
    @PastOrPresent(message = "Present or Past dates only!")
    private LocalDate dateOfIssue;

}

