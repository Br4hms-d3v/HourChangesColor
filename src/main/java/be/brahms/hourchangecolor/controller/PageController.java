package be.brahms.hourchangecolor.controller;

import be.brahms.hourchangecolor.model.HourColorEntity;
import be.brahms.hourchangecolor.service.ColorService;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // This class handles web page requests
public class PageController {

  // Service to get colour data
  private final ColorService colorService;

  // Constructor
  public PageController(ColorService colorService) {
    this.colorService = colorService;
  }

  // This method handles requests to the home page "/"
  @GetMapping("/")
  public String index(Model model) {
    // Get current time
    LocalTime localTime = LocalTime.now();
    // Get current colour and message
    HourColorEntity HourColor = colorService.getCurrentColor();
    // Add data to the model (sent to the view)
    model.addAttribute("backgroundColor", HourColor.getBackgroundColor());
    model.addAttribute("textColor", HourColor.getTextColor());
    model.addAttribute("message", HourColor.getMessage());
    model.addAttribute("time", localTime.truncatedTo(ChronoUnit.SECONDS).toString());

    // return the page index
    return "index";
  }
}
