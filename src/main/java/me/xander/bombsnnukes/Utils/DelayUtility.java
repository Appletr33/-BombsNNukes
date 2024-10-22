package me.xander.bombsnnukes.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DelayUtility {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Delays the execution of a given function by a specified time.
     *
     * @param runnable The function to be executed after the delay.
     * @param delaySeconds The delay in seconds (can be a float for millisecond precision).
     */
    public static void delayFunction(Runnable runnable, float delaySeconds) {
        long delayMillis = (long) (delaySeconds * 1000);
        scheduler.schedule(runnable, delayMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Shuts down the scheduler. Call this method when you're done using the utility
     * to prevent the application from hanging.
     */
    public static void shutdown() {
        scheduler.shutdown();
    }
}