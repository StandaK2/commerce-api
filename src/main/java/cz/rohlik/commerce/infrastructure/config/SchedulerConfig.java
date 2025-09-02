package cz.rohlik.commerce.infrastructure.config;

import static cz.rohlik.commerce.infrastructure.config.LoggingConfig.logger;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;

/**
 * Scheduler configuration for the Commerce API application. Enables scheduled tasks for order
 * expiration processing.
 */
@Configuration
@Profile("!test")
@EnableScheduling
public class SchedulerConfig {

    private static final Logger logger = logger();

    @Bean
    @ConditionalOnThreading(Threading.VIRTUAL)
    public SimpleAsyncTaskScheduler taskScheduler(SimpleAsyncTaskSchedulerBuilder builder) {
        var scheduler = builder.build();
        scheduler.setErrorHandler(throwable -> logger.error("Error in scheduled task", throwable));
        scheduler.setThreadNamePrefix("commerce-api-scheduler-");
        return scheduler;
    }
}
