package cc.rapidev.qqbot.extension.job;

import org.quartz.JobDetail;
import org.quartz.JobKey;


/**
 * @author leibrother
 */
public final class JobHandle {

    private final JobDetail job;
    private final JobController controller;

    public JobHandle(JobDetail job, JobController controller) {
        this.job = job;
        this.controller = controller;
    }

    private JobKey getJobKey() {
        return this.job.getKey();
    }

    public void pause() {
        this.controller.pauseJob(getJobKey());
    }

    public void resume() {
        this.controller.resumeJob(getJobKey());
    }

    public void cancel() {
        this.controller.cancelJob(getJobKey());
    }

}
