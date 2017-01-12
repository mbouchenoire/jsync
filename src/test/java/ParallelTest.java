import com.mbouchenoire.jsync.Jsync;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

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
        final StringBuilder builder = new StringBuilder("hi jsync");

        final List<Runnable> runnables = new ArrayList<Runnable>();

        runnables.add(illegalArgumentRunnable);
        runnables.add(new StringBuilderReverser(builder));
        runnables.add(illegalArgumentRunnable);
        runnables.add(new StringBuilderReverser(builder));
        runnables.add(new StringBuilderReverser(builder));
        runnables.add(illegalArgumentRunnable);
        runnables.add(new StringBuilderReverser(builder));
        runnables.add(new StringBuilderReverser(builder));

        final Set<ExecutionException> errors = Jsync.parallel(runnables);

        assertEquals(3, errors.size());
        assertEquals("cnysj ih", builder.toString());
    }
}
