package hhh.report.service;
import hhh.report.model.Report;
import hhh.report.repository.ReportRepository;
import hhh.web.dto.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void createReport(ReportRequest reportRequest) {
        Report report = Report.builder()
                .troublemaker(reportRequest.getTroublemaker())
                .concernedUser(reportRequest.getConcernedUser())
                .reportType(reportRequest.getReportType())
                .description(reportRequest.getDescription())
                .whereItHappened(reportRequest.getWhereItHappened())
                .dateOfIssue(reportRequest.getDateOfIssue())
                .createdOn(LocalDate.now())
                .reviewed(false)
                .build();

        this.reportRepository.save(report);
    }

    public List<Report> getAllNotReviewedReports() {
       return reportRepository.findByReviewedNot(true);
    }
    public List<Report> getAllReviewedReports() {
        return reportRepository.findByReviewedNot(false);
    }
    public List<Report> getAll(){
        return reportRepository.findAll();
    }
    public List<Report> getAllCreatedLastYear(){
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return reportRepository.findAllByCreatedOnAfter(oneYearAgo);
    }
    public List<Report> getAllCreatedLastMonth(){
        LocalDate oneYearAgo = LocalDate.now().minusMonths(1);
        return reportRepository.findAllByCreatedOnAfter(oneYearAgo);
    }
    public List<Report> getAllCreatedLastWeek(){
        LocalDate oneYearAgo = LocalDate.now().minusDays(7);
        return reportRepository.findAllByCreatedOnAfter(oneYearAgo);
    }
    public List<Report> getAllCreatedLastDay(){
        LocalDate oneYearAgo = LocalDate.now().minusDays(1);
        return reportRepository.findAllByCreatedOnAfter(oneYearAgo);
    }

    public void completeReport(UUID id) {
        Report report = this.reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        report.setReviewed(true);
        reportRepository.save(report);
    }

}
