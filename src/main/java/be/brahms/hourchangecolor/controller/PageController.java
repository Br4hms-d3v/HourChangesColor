package be.brahms.hourchangecolor.controller;

import be.brahms.hourchangecolor.model.HourColorEntity;
import be.brahms.hourchangecolor.service.ColorService;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

  private final ColorService colorService;

  public PageController(ColorService colorService) {
    this.colorService = colorService;
  }

  @GetMapping("/")
  public String index(Model model) {
    LocalTime localTime = LocalTime.now();
    HourColorEntity HourColor = colorService.getCurrentColor();
    model.addAttribute("backgroundColor", HourColor.getBackgroundColor());
    model.addAttribute("textColor", HourColor.getTextColor());
    model.addAttribute("message", HourColor.getMessage());
    model.addAttribute("time", localTime.truncatedTo(ChronoUnit.SECONDS).toString());

    return "index";
  }
}
