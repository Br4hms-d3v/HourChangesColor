package be.brahms.hourchangecolor.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration // This class contains configuration for Spring
@EnableBatchProcessing // This enables batch jobs (Spring batch)
@EnableScheduling // This allows automatic scheduled tasks
public class ColorJobConfig {
  // These variables are used to manage the job
  private final JobRepository jobRepository; // Stores job data
  private final ColorTasklet colorTasklet; // Contains the main task logic
  private final JobOperator jobOperator; // Starts and controls jobs

  // Constructor to initialize all required objects
  public ColorJobConfig(
      JobRepository jobRepository, ColorTasklet colorTasklet, JobOperator jobOperator) {
    this.jobRepository = jobRepository;
    this.colorTasklet = colorTasklet;
    this.jobOperator = jobOperator;
  }

  /**
   * This method creates a step called "colorStep" A step is one part of a job It used the ColorTask
   * to do the work
   *
   * @return a Step object
   */
  @Bean
  public Step colorStep() {
    return new StepBuilder("colorStep", jobRepository).tasklet(colorTasklet).build();
  }

  /**
   * This method creates a job called "colorJob" A job is a complete process This job starts with
   * the step "colorStep"
   *
   * @return a Job object
   */
  @Bean
  public Job colorJob() {
    return new JobBuilder("colorJob", jobRepository).start(colorStep()).build();
  }

  /**
   * This method runs the job automatically The cron "0 * * * * *" means : every minute
   *
   * @throws Exception exception
   */
  @Scheduled(cron = "0 * * * * *")
  public void runJob() throws Exception {
    // Create parameters for the job
    // The current time makes each execution unique
    JobParameters params =
        new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
    // Start the job using the job operator
    jobOperator.start(colorJob(), params);
  }
}
