import com.mbouchenoire.jsync.Jsync;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public void emptyRunnablesShouldReturnEmptyErrors() {
        assertEquals(0, Jsync.parallel(new Runnable[0]).size());
    }

    @Test
    public void workingRunnableShouldNotReturnError() {
        assertEquals(0, Jsync.parallel(printRunnable).size());
    }

    @Test
    public void errorRunnableShouldReturnError() {
        Map<Runnable, Throwable> errors = Jsync.parallel(printRunnable, illegalArgumentRunnable);
        assertEquals(1, errors.size());
        assertTrue(errors.get(illegalArgumentRunnable) instanceof IllegalArgumentException);
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
        final Counter counter = new Counter(0);

        final List<Runnable> runnables = new ArrayList<Runnable>();

        runnables.add(illegalArgumentRunnable);
        runnables.add(new CounterIncremeterRunnable(counter));
        runnables.add(illegalArgumentRunnable);
        runnables.add(new CounterIncremeterRunnable(counter));
        runnables.add(new CounterIncremeterRunnable(counter));
        runnables.add(illegalArgumentRunnable);
        runnables.add(new CounterIncremeterRunnable(counter));

        final Map<Runnable, Throwable> errors = Jsync.parallel(runnables);

        assertEquals(3, errors.size());
        assertEquals(4, counter.getCount());
    }
}
