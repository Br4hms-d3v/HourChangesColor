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

@Component // This class is a Spring component (managed by Spring)
public class ColorTasklet implements Tasklet {

  private final ColorService colorService; // Service to save colour
  private final ApplicationEventPublisher publisher; // Used to send events

  // Constructor to initialize dependencies
  public ColorTasklet(ColorService colorService, ApplicationEventPublisher publisher) {
    this.colorService = colorService;
    this.publisher = publisher;
  }

  // This method is executed when the step runs
  @Override
  public @Nullable RepeatStatus execute(
      @NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) {

    // Get current time
    int hour = LocalDateTime.now().getHour();
    LocalDateTime localTime = LocalDateTime.now();

    // Variables for colour and message
    String backGroundColor;
    String textColor;
    String message;

    // Choose colour and message based on the hour
    if (hour >= 0 && hour <= 5) {
      backGroundColor = "#121212"; // Colour background
      textColor = "#FFFFFF"; // Colour of text
      message = "Bonne nuit"; // Text display on screen
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

    // Save the colour and message
    colorService.saveColors(
        backGroundColor, textColor, message, localTime.truncatedTo(ChronoUnit.MINUTES));
    // Send an event to notify that colour are update
    publisher.publishEvent(new ColorUpdatedEvent());
    // End of the task
    return RepeatStatus.FINISHED;
  }
}
