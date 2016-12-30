import com.mbouchenoire.jsync.Consumer;
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
public class ForEachTest {

    private static final Consumer<Object> printConsumer = new Consumer<Object>() {
        public void accept(Object o) {
            System.out.println("o = [" + o + "]");
        }
    };

    private static final Consumer<Counter> oneSecondSleepConsumer = new Consumer<Counter>() {
        public void accept(Counter c) {
            try {
                Thread.sleep(1000);
                printConsumer.accept(c);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    };

    private static final Consumer<Counter> counterIncrementConsumer = new Consumer<Counter>() {
        public void accept(Counter counter) {
            counter.increment();
        }
    };

    private static final Consumer<Counter> counterErrorConsumer = new Consumer<Counter>() {
        public void accept(Counter counter) {
            if (counter.getCount() == 0) {
                throw new IllegalArgumentException("0 counter");
            } else {
                counterIncrementConsumer.accept(counter);
            }
        }
    };

    private static final List<Counter> newCounters() {
        final List<Counter> counters = new ArrayList<Counter>(2);
        counters.add(new Counter(0));
        counters.add(new Counter(1));
        return counters;
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsArrayShouldThrowException() {
        Object[] objects = null;
        Jsync.forEach(objects, printConsumer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsCollectionShouldThrowException() {
        Collection<Object> objects = null;
        Jsync.forEach(objects, printConsumer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConsumerShouldThrowException() {
        Jsync.forEach(new ArrayList<Object>(), null);
    }

    @Test
    public void consumerShouldConsume() {
        final List<Counter> counters = newCounters();

        Jsync.forEach(counters, counterIncrementConsumer);

        assertEquals(1, counters.get(0).getCount());
        assertEquals(2, counters.get(1).getCount());
    }

    @Test
    public void itemsShouldBeConsumedInParallel() {
        final List<Counter> counters = newCounters();

        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.forEach(counters, oneSecondSleepConsumer);
        stopWatch.stop();

        System.out.println("2 * oneSecondSleepConsumer consumed in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }
}
