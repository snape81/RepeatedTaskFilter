package it.claudiostarnoni.utils.concurrent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RepeatedTask implements Callable<Void> {

    private static final Logger log = LoggerFactory.getLogger(RepeatedTask.class);
    final Callable callable;
    private final String taskKey;
    private final RepeatedTaskLimiterScheduler scheduler;
    boolean endedTask;

    public RepeatedTask(RepeatedTaskLimiterScheduler scheduler, String taskKey, Callable callable) {
        this.taskKey = taskKey;
        this.callable = callable;
        this.scheduler = scheduler;
    }

    @Override
    public synchronized Void call() throws Exception {//synchronized for merge issues
        try {
            log.debug("REP TASK START: Executing repeated task: {}", taskKey);
            callable.call();
            log.debug("REP TASK END: Executing repeated task: {} ", taskKey);
        } catch (Exception e) {
            log.error("END: Error executing repeated task: {}", taskKey, e);
        } finally {
            endedTask = true;
            RepeatedTask nextTask = scheduler.removeScheduledRepeatable(taskKey);
            if (nextTask != null && this != nextTask) {
                scheduler.schedule(nextTask.taskKey, nextTask.callable);
            }
        }
        return null;
    }


}
