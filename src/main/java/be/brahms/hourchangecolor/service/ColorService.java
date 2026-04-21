package be.brahms.hourchangecolor.service;

import be.brahms.hourchangecolor.model.HourColorEntity;
import be.brahms.hourchangecolor.repository.ColorRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service // This class contains business logic
public class ColorService {
  // Repository to access the database
  private final ColorRepository colorRepository;

  // Constructor
  public ColorService(ColorRepository colorRepository) {
    this.colorRepository = colorRepository;
  }

  // This method saves colors, message, and time in the database
  public void saveColors(
      String backgroundColor, String textColor, String message, LocalDateTime time) {

    // Create a new entity
    HourColorEntity hourColorEntity = new HourColorEntity();
    // Set values
    hourColorEntity.setBackgroundColor(backgroundColor);
    hourColorEntity.setTextColor(textColor);
    hourColorEntity.setMessage(message);
    hourColorEntity.setUpdateAt(time);
    // Save in database
    colorRepository.save(hourColorEntity);
  }

  // This method gets the latest color from the database
  public HourColorEntity getCurrentColor() {
    return colorRepository
        .findLatest()
        .orElseGet(
            () -> {
              // Default values if nothing is found
              HourColorEntity entity = new HourColorEntity();
              entity.setBackgroundColor("#FFFFFF");
              entity.setTextColor("#000000");
              entity.setMessage("Bonne journée");
              return entity;
            });
  }
}
