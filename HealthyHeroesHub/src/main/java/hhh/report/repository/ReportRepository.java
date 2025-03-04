package hhh.report.repository;
import hhh.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByReviewedNot(boolean reviewed);
    List<Report> findAllByCreatedOnAfter(LocalDate createdOnAfter);
}
