package hhh.web;


import hhh.calculator.CalculatorService;
import hhh.web.dto.CalculatorRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping()
    public ModelAndView getCalculator(CalculatorRequest calculatorRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("calculatorRequest",calculatorRequest);
        modelAndView.setViewName("calculator");

        return modelAndView;
    }
    @PostMapping()
    public ModelAndView postCalculator(@Valid CalculatorRequest calculatorRequest, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if(bindingResult.hasErrors()) {
            modelAndView.setViewName("calculator");
            modelAndView.addObject("calculatorRequest",calculatorRequest);
            return modelAndView;
        }
        modelAndView.addObject("calculatorRequest",calculatorRequest);
        modelAndView.setViewName("calculator_results");
        String message = calculatorService.calculateDailyCalories(calculatorRequest.getWeight(), calculatorRequest.getHeight(), calculatorRequest.getAge(), calculatorRequest.getGender(), calculatorRequest.getActivityLevel(), calculatorRequest.getEatenCalories());
        modelAndView.addObject("message",message);
        return modelAndView;
    }




}
