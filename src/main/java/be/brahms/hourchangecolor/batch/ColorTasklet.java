package be.brahms.hourchangecolor.batch;

import be.brahms.hourchangecolor.event.ColorUpdatedEvent;
import be.brahms.hourchangecolor.service.ColorService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ColorTasklet implements Tasklet {

  private final ColorService colorService;
  private final ApplicationEventPublisher publisher;

  public ColorTasklet(ColorService colorService, ApplicationEventPublisher publisher) {
    this.colorService = colorService;
    this.publisher = publisher;
  }

  @Override
  public @Nullable RepeatStatus execute(
      @NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
    int hour = LocalDateTime.now().getHour();
    LocalDateTime localTime = LocalDateTime.now();

    String backGroundColor;
    String textColor;
    String message;

    if (hour >= 0 && hour <= 5) {
      backGroundColor = "#121212";
      textColor = "#FFFFFF";
      message = "Bonne nuit";
    } else if (hour <= 11) {
      backGroundColor = "#E0F7FA";
      textColor = "#000000";
      message = "Bonjour";
    } else if (hour <= 17) {
      backGroundColor = "#FFF9C4";
      textColor = "#000000";
      message = "Bon après-midi";
    } else {
      backGroundColor = "#FFCCBC";
      textColor = "#FFFFFF";
      message = "Bonne soirée";
    }

    colorService.saveColors(
        backGroundColor, textColor, message, localTime.truncatedTo(ChronoUnit.MINUTES));
    publisher.publishEvent(new ColorUpdatedEvent());

    return RepeatStatus.FINISHED;
  }
}
