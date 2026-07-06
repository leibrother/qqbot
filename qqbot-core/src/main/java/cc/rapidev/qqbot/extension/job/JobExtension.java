package cc.rapidev.qqbot.extension.job;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;

import java.util.Properties;

/**
 * @author leibrother
 */
public class JobExtension implements Extension {

    private JobService service;

    @Override
    public void ready(Bot bot) {
        Properties props = new Properties();
        props.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        props.setProperty("org.quartz.threadPool.threadCount", "10");
        props.setProperty("org.quartz.threadPool.threadPriority", "5");
        props.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        this.service = new JobService(props);
        bot.add(service);
    }

    @Override
    public void destroy() throws Exception {
        if (this.service != null) {
            this.service.destroy();
        }
    }

}
