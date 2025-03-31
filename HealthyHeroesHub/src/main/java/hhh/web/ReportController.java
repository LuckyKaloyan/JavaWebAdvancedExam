package hhh.web;
import hhh.report.service.ReportService;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.ReportRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserService userService;

    @Autowired
    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }
    @GetMapping("/my_reports")
    public ModelAndView getReports(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("my_reports");
        return modelAndView;
    }

    @GetMapping("/create_new_report")
    public ModelAndView getNewReport(ReportRequest reportRequest){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create_new_report");
        modelAndView.addObject("reportRequest", reportRequest);
        return modelAndView;
    }

    @PostMapping("/create_new_report")
    public ModelAndView postNewReport(@Valid ReportRequest reportRequest, BindingResult result, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("create_new_report");
            modelAndView.addObject("reportRequest", reportRequest);
            return modelAndView;
        }
        reportService.createReport(reportRequest, authenticationDetails.getId());
        return new ModelAndView("redirect:/home");
    }
}
