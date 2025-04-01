package hhh.report;
import hhh.exception.BadInputException;
import hhh.report.model.Report;
import hhh.report.model.ReportLocation;
import hhh.report.model.ReportType;
import hhh.report.repository.ReportRepository;
import hhh.report.service.ReportService;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.ReportRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceUnitTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReportService reportService;

    @Test
    public void createReport_ThrowsWhenReportRequestIsNull() {
        assertThrows(BadInputException.class, () -> reportService.createReport(null, UUID.randomUUID()));
    }

    @Test
    public void createReport_ThrowsWhenUserIdIsNull() {
        ReportRequest request = new ReportRequest();
        assertThrows(BadInputException.class, () -> reportService.createReport(request, null));
    }

    @Test
    public void createReport_ThrowsWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userService.getById(userId)).thenReturn(null);
        assertThrows(BadInputException.class, () -> reportService.createReport(new ReportRequest(), userId));
    }

    @Test
    public void createReport_SavesReportWhenInputValid() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        ReportRequest request = new ReportRequest();
        request.setTroublemaker("troublemaker");
        request.setReportType(ReportType.FRAUD);
        request.setDescription("desc");
        request.setWhereItHappened(ReportLocation.COMMENT);
        request.setDateOfIssue(LocalDate.now());
        when(userService.getById(userId)).thenReturn(user);
        when(reportRepository.save(any(Report.class))).thenReturn(new Report());
        reportService.createReport(request, userId);
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    public void getAllNotReviewedReports_ReturnsNotReviewedReports() {
        when(reportRepository.findByReviewedNot(true)).thenReturn(List.of(new Report()));
        assertEquals(1, reportService.getAllNotReviewedReports().size());
    }

    @Test
    public void getAllReviewedReports_ReturnsReviewedReports() {
        when(reportRepository.findByReviewedNot(false)).thenReturn(List.of(new Report()));
        assertEquals(1, reportService.getAllReviewedReports().size());
    }

    @Test
    public void getAll_ReturnsAllReports() {
        when(reportRepository.findAll()).thenReturn(List.of(new Report(), new Report()));
        assertEquals(2, reportService.getAll().size());
    }

    @Test
    public void completeReport_ThrowsWhenIdIsNull() {
        assertThrows(BadInputException.class, () -> reportService.completeReport(null));
    }

    @Test
    public void completeReport_ThrowsWhenReportNotFound() {
        UUID id = UUID.randomUUID();
        when(reportRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> reportService.completeReport(id));
    }

    @Test
    public void completeReport_SetsReviewedToTrue() {
        UUID id = UUID.randomUUID();
        Report report = new Report();
        report.setReviewed(false);
        when(reportRepository.findById(id)).thenReturn(Optional.of(report));
        reportService.completeReport(id);
        assertTrue(report.isReviewed());
        verify(reportRepository).save(report);
    }

    @Test
    public void getAllCreatedLastYear_ReturnsReportsFromLastYear() {
        when(reportRepository.findAllByCreatedOnAfter(any(LocalDate.class))).thenReturn(List.of(new Report()));
        assertEquals(1, reportService.getAllCreatedLastYear().size());
    }

    @Test
    public void getAllCreatedLastMonth_ReturnsReportsFromLastMonth() {
        when(reportRepository.findAllByCreatedOnAfter(any(LocalDate.class))).thenReturn(List.of(new Report()));
        assertEquals(1, reportService.getAllCreatedLastMonth().size());
    }

    @Test
    public void getAllCreatedLastWeek_ReturnsReportsFromLastWeek() {
        when(reportRepository.findAllByCreatedOnAfter(any(LocalDate.class))).thenReturn(List.of(new Report()));
        assertEquals(1, reportService.getAllCreatedLastWeek().size());
    }

    @Test
    public void getAllCreatedLastDay_ReturnsReportsFromLastDay() {
        when(reportRepository.findAllByCreatedOnAfter(any(LocalDate.class))).thenReturn(List.of(new Report()));
        assertEquals(1, reportService.getAllCreatedLastDay().size());
    }
}