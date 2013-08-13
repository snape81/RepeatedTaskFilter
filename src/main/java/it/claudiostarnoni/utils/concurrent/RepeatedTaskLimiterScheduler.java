package it.claudiostarnoni.utils.concurrent;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * This class manages repeated idempotent tasks in an event driven
 * app architecture.
 * <p/>
 * It avoids to start the same task at same time, enqueues only one task
 * per key and waits for completion of the executing task.
 */
public interface RepeatedTaskLimiterScheduler {

    <T> boolean schedule(String taskKey, Callable<T> callable);

    @PreDestroy
    void destroy() throws IOException;

    RepeatedTask removeScheduledRepeatable(String taskKey);
}
