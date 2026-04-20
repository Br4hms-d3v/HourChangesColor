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
      backGroundColor = "#0B1320";
      textColor = "#FFFFFF";
      message = "Bonne nuit \uD83C\uDF04 ";
    } else if (hour <= 11) {
      backGroundColor = "#F5F5F5";
      textColor = "#000000";
      message = "Bonjour \uD83C\uDF05";
    } else if (hour <= 17) {
      backGroundColor = "##FFD166";
      textColor = "#000000";
      message = "Bon après-midi \uD83C\uFE0F";
    } else {
      backGroundColor = "#1C1C2E";
      textColor = "#FFFFFF";
      message = "Bonne soirée \uD83C\uDF06";
    }

    colorService.saveColors(
        backGroundColor, textColor, message, localTime.truncatedTo(ChronoUnit.MINUTES));
    publisher.publishEvent(new ColorUpdatedEvent());

    return RepeatStatus.FINISHED;
  }
}
