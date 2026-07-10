package cc.rapidev.qqbot.rocokingdom.job;

import cc.rapidev.qqbot.extension.job.annotations.JobRemark;
import cc.rapidev.qqbot.rocokingdom.service.MerchantService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * @author leibrother
 */
@JobRemark("远行商人推送")
public class MerchantPushJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        MerchantService service = (MerchantService) data.get("service");
        service.pushNowadaysRound();
    }

}
