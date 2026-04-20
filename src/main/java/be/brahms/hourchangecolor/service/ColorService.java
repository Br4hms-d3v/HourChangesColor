package be.brahms.hourchangecolor.service;

import be.brahms.hourchangecolor.model.HourColorEntity;
import be.brahms.hourchangecolor.repository.ColorRepository;
import org.springframework.stereotype.Service;

@Service
public class ColorService {

  private final ColorRepository colorRepository;

  public ColorService(ColorRepository colorRepository) {
    this.colorRepository = colorRepository;
  }

  public void saveColors(String backgroundColor, String textColor, String message) {
    HourColorEntity hourColorEntity = new HourColorEntity();
    hourColorEntity.setBackgroundColor(backgroundColor);
    hourColorEntity.setTextColor(textColor);
    hourColorEntity.setMessage(message);
    colorRepository.save(hourColorEntity);
  }

  public HourColorEntity getCurrentColor() {
    return colorRepository
        .findLatest()
        .orElseGet(
            () -> {
              HourColorEntity entity = new HourColorEntity();
              entity.setBackgroundColor("#FFFFFF");
              entity.setTextColor("#000000");
              entity.setMessage("Bonne journée");
              return entity;
            });
  }
}
