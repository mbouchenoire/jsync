import com.mbouchenoire.jsync.Jsync;
import com.mbouchenoire.jsync.Parallel;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mbouchenoire
 */
public final class ParallelTest {

    private static final Runnable printRunnable = new Runnable() {
        public void run() {
            System.out.println("runnable done");
        }
    };

    private static final Runnable illegalArgumentRunnable = new Runnable() {
        public void run() {
            throw new IllegalArgumentException("error from runnable");
        }
    };

    private static final Runnable oneSecondSleepRunnable = new Runnable() {
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    };

    @Test(expected = IllegalArgumentException.class)
    public void nullExecutorServiceShouldThrowException() {
        new Parallel(null, 1, null);
        assertTrue(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidTimeOutShouldThrowException() {
        new Parallel(Executors.newSingleThreadExecutor(), -1, null);
        assertTrue(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRunnablesArrayShouldThrowException() {
        Runnable[] nullRunnablesArray = null;
        Jsync.parallel(nullRunnablesArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRunnablesCollectionShouldThrowException() {
        Collection<Runnable> nullRunnablesCollection = null;
        Jsync.parallel(nullRunnablesCollection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void anyNullRenableShouldThrowException() {
        Jsync.parallel(printRunnable, null);
    }

    @Test
    public void runnablesShouldRunInParallel() {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.parallel(oneSecondSleepRunnable, oneSecondSleepRunnable);
        stopWatch.stop();

        System.out.println("2 * oneSecondSleepRunnable runned in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }

    @Test
    public void errorRunnablesShouldNotInterruptWorkingRunnables() {
        final StringBuffer buffer = new StringBuffer("hi jsync");

        final List<Runnable> runnables = new ArrayList<Runnable>();

        runnables.add(illegalArgumentRunnable);
        runnables.add(new StringBufferReverser(buffer));
        runnables.add(illegalArgumentRunnable);
        runnables.add(new StringBufferReverser(buffer));
        runnables.add(new StringBufferReverser(buffer));
        runnables.add(illegalArgumentRunnable);
        runnables.add(new StringBufferReverser(buffer));
        runnables.add(new StringBufferReverser(buffer));

        Jsync.parallel(runnables);

        assertEquals("cnysj ih", buffer.toString());
    }
}
