package it.claudiostarnoni.concurrent.test;


import it.claudiostarnoni.utils.concurrent.RepeatedTaskLimiterScheduler;
import it.claudiostarnoni.utils.concurrent.RepeatedTaskLimiterSchedulerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class RepeatedTaskLimiterSchedulerTest {
    private final Logger logWriter = LoggerFactory.getLogger(RepeatedTaskLimiterSchedulerTest.class);
    private RepeatedTaskLimiterScheduler rtlms;

    @Before
    public void setUp() throws Exception {
        rtlms = new RepeatedTaskLimiterSchedulerImpl(4);
    }

    @After
    public void tearDown() throws Exception {
        rtlms.destroy();
    }

    @Test
    public void testSchedule_repeatedTask() throws IOException, InterruptedException {
        logWriter.debug("REPEATED TASK AVOIDING TEST ");
        LongCalculationTaskCallable lt1 = new LongCalculationTaskCallable();

        List<LongCalculationTaskCallable> taskList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            taskList.add(new LongCalculationTaskCallable());
        }

        logWriter.debug("scheduled {}", rtlms.schedule("a", lt1));
        for (LongCalculationTaskCallable longCalculationTaskCallable : taskList) {
            logWriter.debug("scheduled {}", rtlms.schedule("a", longCalculationTaskCallable));
            Thread.sleep(100);
        }

        Thread.sleep(15 * 1000);
        assertThat("The LongCalculation is exactly repeated only twice", Counter.getQuantity(), equalTo(2));
    }

    private static class LongCalculationTaskCallable implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            Thread.sleep(5000);
            Counter.addHit();
            return null;
        }
    }

    private static class Counter {
        private static int quantity = 0;

        private static int getQuantity() {
            return quantity;
        }

        private static void addHit() {
            Counter.quantity++;
        }
    }

}
