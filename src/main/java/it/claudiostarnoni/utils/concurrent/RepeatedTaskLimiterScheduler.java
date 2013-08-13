package it.claudiostarnoni.utils.concurrent;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * This class manage repetaed idempotent task that can came froma an event driven
 * app architecture.
 *
 * It Avoids to start the same operation in same time, enqueue only one action
 * per key and wait for completion of action in execution
 *
 */
public interface RepeatedTaskLimiterScheduler {

    <T> boolean schedule(String taskKey, Callable<T> callable);

    @PreDestroy
    void destroy() throws IOException;
}
