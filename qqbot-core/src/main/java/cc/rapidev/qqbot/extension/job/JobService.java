package cc.rapidev.qqbot.extension.job;

import cc.rapidev.qqbot.common.interfaces.Disposable;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leibrother
 */
public class JobService implements JobController, Disposable {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);
    private final Scheduler scheduler;
    private final AtomicInteger jobIdGenerator = new AtomicInteger(0);

    public JobService(Properties properties) {
        try {
            StdSchedulerFactory factory = new StdSchedulerFactory(properties);
            Scheduler scheduler = factory.getScheduler();
            scheduler.start();
            this.scheduler = scheduler;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private JobKey nextJobKey() {
        int id = this.jobIdGenerator.incrementAndGet();
        return JobKey.jobKey("job_" + id, "bot");
    }

    private TriggerKey createTriggerKey(JobKey jobKey) {
        return new TriggerKey(jobKey.getGroup() + "_" + jobKey.getName() + "_trigger", jobKey.getGroup());
    }

    private Trigger buildTrigger(JobDetail jobDetail, ScheduleBuilder<?> scheduleBuilder, int priority) {
        JobKey jobKey = jobDetail.getKey();
        return TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey(jobKey.getGroup() + "_" + jobKey.getName() + "_trigger", jobKey.getGroup()))
                .withSchedule(scheduleBuilder)
                .withPriority(priority)
                .build();
    }

    public JobHandle addJob(Class<? extends Job> jobClass, JobDataMap data, TriggerBuilder<?> triggerBuilder) {
        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(nextJobKey())
                .setJobData(data == null ? new JobDataMap() : data)
                .build();
        Trigger trigger = triggerBuilder.withIdentity(createTriggerKey(job.getKey())).build();
        try {
            this.scheduler.scheduleJob(job, trigger);
            return new JobHandle(job, this);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public JobHandle addCronJob(Class<? extends Job> jobClass, JobDataMap data, String cron) {
        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron));
        return addJob(jobClass, data, triggerBuilder);
    }

    public JobHandle addIntervalJob(Class<? extends Job> jobClass, JobDataMap data, int seconds) {
        return addIntervalJob(jobClass, data, seconds, null);
    }

    public JobHandle addIntervalJob(Class<? extends Job> jobClass, JobDataMap data, int seconds, Instant startAt) {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        scheduleBuilder.withIntervalInSeconds(seconds);
        scheduleBuilder.repeatForever();
        TriggerBuilder<SimpleTrigger> builder = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder);
        if (startAt != null) {
            builder.startAt(startAt);
        }
        return addJob(jobClass, data, builder);
    }

    @Override
    public void pauseJob(JobKey key) {
        try {
            this.scheduler.pauseJob(key);
            logger.debug("pause job {}", key);
        } catch (SchedulerException e) {
            logger.error("pause job error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resumeJob(JobKey key) {
        try {
            this.scheduler.resumeJob(key);
            logger.debug("resume job {}", key);
        } catch (SchedulerException e) {
            logger.error("resume job error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelJob(JobKey key) {
        try {
            this.scheduler.deleteJob(key);
            logger.debug("cancel job {}", key);
        } catch (SchedulerException e) {
            logger.error("cancel job error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        try {
            this.scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

}
