package hhh.web;


import hhh.report.service.ReportService;
import hhh.web.dto.RegisterRequest;
import hhh.web.dto.ReportRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/create_new_report")
    public ModelAndView getNewReport(ReportRequest reportRequest){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create_new_report");
        modelAndView.addObject("reportRequest", reportRequest);
        return modelAndView;
    }

    @PostMapping("/create_new_report")
    public ModelAndView postNewReport(@Valid ReportRequest reportRequest, BindingResult result) {
        if(result.hasErrors()) {
            return new ModelAndView("create_new_report");
        }
        reportService.createReport(reportRequest);
        return new ModelAndView("redirect:/home");
    }
}
