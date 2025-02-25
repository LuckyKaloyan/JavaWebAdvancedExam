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
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private User concernedUser;
    @ManyToOne
    private User troublemaker;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    @Enumerated(EnumType.STRING)
    private ReportLocation whereItHappened;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDate createdOn;


}
