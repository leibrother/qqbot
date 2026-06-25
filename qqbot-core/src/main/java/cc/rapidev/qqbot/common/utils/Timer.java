package cc.rapidev.qqbot.common.utils;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author leibrother
 */
public class Timer {

    public static long take(Runnable runnable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        runnable.run();
        stopWatch.stop();
        return stopWatch.getTime(TimeUnit.MILLISECONDS);
    }

}
