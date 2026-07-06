package cc.rapidev.qqbot.extension.job;

import org.quartz.JobKey;

/**
 * @author leibrother
 */
public interface JobController {

    void pauseJob(JobKey key);

    void resumeJob(JobKey key);

    void cancelJob(JobKey key);

}
