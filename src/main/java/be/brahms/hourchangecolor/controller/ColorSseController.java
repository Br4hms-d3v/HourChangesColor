package be.brahms.hourchangecolor.controller;

import be.brahms.hourchangecolor.event.ColorUpdatedEvent;
import be.brahms.hourchangecolor.model.HourColorEntity;
import be.brahms.hourchangecolor.service.ColorService;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController // This class handles HTTP requests
public class ColorSseController {

  // Service to get colour data
  private final ColorService colorService;
  // List of connected clients
  private final List<SseEmitter> emitters = new ArrayList<>();

  // Constructor
  public ColorSseController(ColorService colorService) {
    this.colorService = colorService;
  }

  // This endpoint allows clients to connect and receive updates
  @GetMapping(path = "/sse/colors", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter streamColors() {
    // Create a new SSE connection (no timeout)
    SseEmitter emitter = new SseEmitter(0L);
    // Add the client to the list
    emitters.add(emitter);
    // Remove client when connection is closed
    emitter.onCompletion(() -> emitters.remove(emitter));
    // Remove client if timeout happens
    emitter.onTimeout(() -> emitters.remove(emitter));

    return emitter;
  }

  // This method is called when colours are update
  @EventListener
  public void onColorUpdated(ColorUpdatedEvent event) {
    // Get current colour
    HourColorEntity hourColor = colorService.getCurrentColor();
    LocalTime localTime = LocalTime.now();

    // Create data to send
    Map<String, String> payload =
        Map.of(
            "backgroundColor", hourColor.getBackgroundColor(),
            "textColor", hourColor.getTextColor(),
            "message", hourColor.getMessage(),
            "time", localTime.truncatedTo(ChronoUnit.SECONDS).toString());

    // Send data to all connected clients
    emitters.forEach(
        emitter -> {
          try {
            emitter.send(SseEmitter.event().name("color").data(payload));
          } catch (Exception ex) {
            emitter.complete();
          }
        });
  }
}
