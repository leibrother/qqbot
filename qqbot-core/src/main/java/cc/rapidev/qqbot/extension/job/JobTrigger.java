package cc.rapidev.qqbot.extension.job;

import org.quartz.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author leibrother
 */
public class JobTrigger {

    private final ScheduleBuilder<?> scheduleBuilder;
    private Date startAt = new Date();

    private JobTrigger(ScheduleBuilder<?> scheduleBuilder) {
        this.scheduleBuilder = scheduleBuilder;
    }

    public JobTrigger delay(int delay) {
        return delay(delay, TimeUnit.MINUTES);
    }

    public JobTrigger delay(int delay, TimeUnit unit) {
        long millis = unit.toMillis(delay);
        this.startAt = new Date(System.currentTimeMillis() + millis);
        return this;
    }

    public Trigger build(TriggerKey key) {
        TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger();
        builder.withIdentity(key);
        builder.withSchedule(scheduleBuilder);
        builder.startAt(startAt);
        return builder.build();
    }

    public static JobTrigger once() {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule();
        return new JobTrigger(builder);
    }

    public static JobTrigger cron(String cron) {
        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(cron);
        return new JobTrigger(builder);
    }

    public static JobTrigger interval(int interval) {
        return interval(interval, TimeUnit.MINUTES, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    public static JobTrigger interval(int interval, TimeUnit unit) {
        return interval(interval, unit, SimpleTrigger.REPEAT_INDEFINITELY);
    }

    public static JobTrigger interval(int interval, TimeUnit unit, int repeat) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule();
        builder.withIntervalInMinutes((int) unit.toMinutes(interval));
        builder.withRepeatCount(repeat);
        return new JobTrigger(builder);
    }

}
