package it.claudiostarnoni.utils.concurrent;

import com.google.common.collect.MapMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepeatedTaskLimiterSchedulerImpl implements RepeatedTaskLimiterScheduler {

    private static final Logger log = LoggerFactory.getLogger(RepeatedTaskLimiterSchedulerImpl.class);

      private final ExecutorService threadPoolExecutor;
      protected final ConcurrentMap<String, RepeatedTask> scheduledRepeatable;

      public RepeatedTaskLimiterSchedulerImpl(int threadPoolSize) {
          this.threadPoolExecutor = Executors.newFixedThreadPool(threadPoolSize);
          this.scheduledRepeatable = new MapMaker().concurrencyLevel(threadPoolSize).makeMap();
      }

    @Override
    public <T> boolean schedule(final String taskKey, final Callable<T> callable) {
        boolean scheduled = false;

        RepeatedTask newTask = new RepeatedTask(this, taskKey, callable);
        RepeatedTask task = scheduledRepeatable.put(taskKey,newTask);
        if (task == null) {
            log.debug("New task created and scheduled {}",newTask);
            // nuovo task per la key
            threadPoolExecutor.submit(newTask);
            scheduled = true;
        }
        return scheduled;
    }


    @Override
    @PreDestroy
      public void destroy() throws IOException {
          log.info("Shutting down ExecutorService");
          try {
              threadPoolExecutor.shutdown();
          } catch (Exception e) {
              log.error("Error shutting down ExecutorService.", e);
          }
      }

}
