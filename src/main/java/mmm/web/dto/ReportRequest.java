package mmm.web.dto;

import mmm.report.model.ReportLocation;
import mmm.report.model.ReportType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

    // No-args constructor
    public ReportRequest() {
    }

    // All-args constructor
    public ReportRequest(
            String troublemaker,
            ReportType reportType,
            ReportLocation whereItHappened,
            String description,
            LocalDate dateOfIssue
    ) {
        this.troublemaker = troublemaker;
        this.reportType = reportType;
        this.whereItHappened = whereItHappened;
        this.description = description;
        this.dateOfIssue = dateOfIssue;
    }

    // Getters and setters

    public String getTroublemaker() {
        return troublemaker;
    }

    public void setTroublemaker(String troublemaker) {
        this.troublemaker = troublemaker;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportLocation getWhereItHappened() {
        return whereItHappened;
    }

    public void setWhereItHappened(ReportLocation whereItHappened) {
        this.whereItHappened = whereItHappened;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }
}
