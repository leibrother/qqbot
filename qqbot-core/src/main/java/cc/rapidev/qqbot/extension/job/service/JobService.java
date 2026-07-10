package cc.rapidev.qqbot.extension.job.service;

import cc.rapidev.qqbot.common.interfaces.Disposable;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.job.JobController;
import cc.rapidev.qqbot.extension.job.JobHandle;
import cc.rapidev.qqbot.extension.job.JobTrigger;
import cc.rapidev.qqbot.extension.job.annotations.JobRemark;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
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

    /**
     * 生成下一个JobKey
     * <p>通过{@code jobIdGenerator}累加</p>
     *
     * @return JobKey
     */
    private JobKey nextJobKey() {
        int id = this.jobIdGenerator.incrementAndGet();
        return JobKey.jobKey("job_%03d".formatted(id), "bot");
    }

    /**
     * 创建TriggerKey
     *
     * @param jobKey JobKey
     * @return TriggerKey
     */
    private TriggerKey createTriggerKey(JobKey jobKey) {
        return new TriggerKey(jobKey.getGroup() + "_" + jobKey.getName() + "_trigger", jobKey.getGroup());
    }

    /**
     * 获取Job详情
     *
     * @param key JobKey
     * @return Job详情
     */
    @NonNull
    private JobDetail getJobDetail(@NotNull JobKey key) {
        try {
            JobDetail jobDetail = this.scheduler.getJobDetail(key);
            if (jobDetail == null) {
                throw new RuntimeException("unknown job: " + key);
            }
            return jobDetail;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Job名称
     *
     * @param job Job详情
     * @return JobKey + JobDescription
     */
    @NotNull
    private String getJobName(@NonNull JobDetail job) {
        JobKey key = job.getKey();
        String description = job.getDescription();
        StringBuilder builder = new StringBuilder(key.toString());
        if (StringUtils.isNotEmpty(description)) {
            builder.append(" (").append(description).append(")");
        }
        return builder.toString();
    }

    /**
     * 添加一个任务
     *
     * @param jobClass   任务类
     * @param jobTrigger 任务触发器
     * @return 任务句柄
     */
    public JobHandle addJob(Class<? extends Job> jobClass, JobTrigger jobTrigger) {
        return addJob(jobClass, new JobDataMap(), jobTrigger);
    }

    /**
     * 添加一个任务
     *
     * @param jobClass   任务类
     * @param data       任务数据字典
     * @param jobTrigger 任务触发器
     * @return 任务句柄
     */
    public JobHandle addJob(Class<? extends Job> jobClass, JobDataMap data, JobTrigger jobTrigger) {
        JobRemark remark = jobClass.getAnnotation(JobRemark.class);
        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(nextJobKey())
                .withDescription(remark == null ? "" : remark.value())
                .setJobData(Objects.requireNonNullElseGet(data, JobDataMap::new))
                .build();
        TriggerKey triggerKey = createTriggerKey(job.getKey());
        Trigger trigger = jobTrigger.build(triggerKey);
        try {
            this.scheduler.scheduleJob(job, trigger);
            logger.info("add job {}", getJobName(job));
            return new JobHandle(job, this);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 暂停Job
     *
     * @param key JobKey
     */
    @Override
    public void pauseJob(@NotNull JobKey key) {
        JobDetail job = getJobDetail(key);
        try {
            this.scheduler.pauseJob(key);
            logger.info("pause job {}", getJobName(job));
        } catch (SchedulerException e) {
            logger.error("pause job {} error", getJobName(job), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 恢复Job
     *
     * @param key JobKey
     */
    @Override
    public void resumeJob(@NotNull JobKey key) {
        JobDetail job = getJobDetail(key);
        try {
            this.scheduler.resumeJob(key);
            logger.info("resume job {}", getJobName(job));
        } catch (SchedulerException e) {
            logger.error("resume job {} error", getJobName(job), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 取消Job
     *
     * @param key JobKey
     */
    @Override
    public void cancelJob(@NotNull JobKey key) {
        JobDetail job = getJobDetail(key);
        try {
            this.scheduler.deleteJob(key);
            logger.info("cancel job {}", getJobName(job));
        } catch (SchedulerException e) {
            logger.error("cancel job {} error", getJobName(job), e);
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
