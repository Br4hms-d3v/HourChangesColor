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
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class ColorJobConfig {
  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final ColorTasklet colorTasklet;
  private final JobOperator jobOperator;

  public ColorJobConfig(
      JobRepository jobRepository,
      PlatformTransactionManager platformTransactionManager,
      ColorTasklet colorTasklet,
      JobOperator jobOperator) {
    this.jobRepository = jobRepository;
    this.platformTransactionManager = platformTransactionManager;
    this.colorTasklet = colorTasklet;
    this.jobOperator = jobOperator;
  }

  @Bean
  public Step colorStep() {
    return new StepBuilder("colorStep", jobRepository).tasklet(colorTasklet).build();
  }

  @Bean
  public Job colorJob() {
    return new JobBuilder("colorJob", jobRepository).start(colorStep()).build();
  }

  @Scheduled(cron = "0 * * * * *")
  public void runJob() throws Exception {
    JobParameters params =
        new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

    jobOperator.start(colorJob(), params);
  }
}
