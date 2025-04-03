package hhh.report.model;
import hhh.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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


}
