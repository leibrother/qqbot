package cc.rapidev.qqbot.common.utils;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * @author leibrother
 */
public class Timer {

    public static long take(Runnable runnable) {
        Stopwatch stopWatch = Stopwatch.createStarted();
        runnable.run();
        stopWatch.stop();
        return stopWatch.elapsed(TimeUnit.MILLISECONDS);
    }

}
