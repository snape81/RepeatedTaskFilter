package it.claudiostarnoni.utils.concurrent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RepeatedTask implements Callable<Void> {

    private static final Logger log = LoggerFactory.getLogger(RepeatedTask.class);
    final Callable callable;
    private final String taskKey;
    private final RepeatedTaskLimiterSchedulerImpl scheduler;
    boolean endedTask;

    public RepeatedTask(RepeatedTaskLimiterSchedulerImpl scheduler, String taskKey, Callable callable) {
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
            return null;
        } catch (Exception e) {
            log.error("END: Error executing repeated task:{}." + taskKey + e);
            return null;
        } finally {
            endedTask = true;
            RepeatedTask nextTask = scheduler.scheduledRepeatable.remove(taskKey);
            if (nextTask != null && this != nextTask) {
                scheduler.schedule(nextTask.taskKey, nextTask.callable);
            }
        }
    }


}
