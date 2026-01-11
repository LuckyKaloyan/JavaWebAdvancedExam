package mmm.report.model;

import mmm.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String troublemaker;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    private ReportLocation whereItHappened;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate dateOfIssue;

    @Column(nullable = false)
    private LocalDate createdOn;

    private boolean reviewed;

    @ManyToOne
    private User concernedUser;

    // No-args constructor (required by JPA)
    public Report() {
    }

    // All-args constructor
    public Report(
            UUID id,
            String troublemaker,
            ReportType reportType,
            ReportLocation whereItHappened,
            String description,
            LocalDate dateOfIssue,
            LocalDate createdOn,
            boolean reviewed,
            User concernedUser
    ) {
        this.id = id;
        this.troublemaker = troublemaker;
        this.reportType = reportType;
        this.whereItHappened = whereItHappened;
        this.description = description;
        this.dateOfIssue = dateOfIssue;
        this.createdOn = createdOn;
        this.reviewed = reviewed;
        this.concernedUser = concernedUser;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public User getConcernedUser() {
        return concernedUser;
    }

    public void setConcernedUser(User concernedUser) {
        this.concernedUser = concernedUser;
    }
}
