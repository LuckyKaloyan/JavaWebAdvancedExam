package hhh.web.dto;

import hhh.user.model.User;
import hhh.report.model.ReportType;
import hhh.report.model.ReportLocation;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {

    @NotNull
    private String concernedUser;
    @NotNull
    private String troublemaker;
    @NotNull
    private ReportType reportType;
    @NotNull
    private ReportLocation whereItHappened;
    @NotNull
    private String description;
    @NotNull
    private LocalDate dateOfIssue;

}

