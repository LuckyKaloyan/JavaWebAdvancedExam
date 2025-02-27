package hhh.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportController {


    @GetMapping("/create_new_report")
    public String createNewReport() {
        return "reports/create_new_report";
    }
}
