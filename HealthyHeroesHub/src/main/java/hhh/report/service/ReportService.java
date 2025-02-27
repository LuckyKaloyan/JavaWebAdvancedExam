package hhh.report.service;
import hhh.report.model.Report;
import hhh.report.repository.ReportRepository;
import hhh.web.dto.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReportService {

    private ReportRepository reportRepository;

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
                .build();

        this.reportRepository.save(report);
    }

}
