import com.mbouchenoire.jsync.Consumer;
import com.mbouchenoire.jsync.Jsync;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private static final Consumer<StringBuilder> oneSecondSleepConsumer = new Consumer<StringBuilder>() {
        public void accept(StringBuilder arg) {
            try {
                Thread.sleep(1000);
                printConsumer.accept(arg);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    };

    private static final Consumer<StringBuilder> stringBuilderReverserConsumer = new Consumer<StringBuilder>() {
        public void accept(StringBuilder arg) {
            arg.reverse();
        }
    };

    private static final Consumer<StringBuilder> stringBuilderErrorConsumer = new Consumer<StringBuilder>() {
        public void accept(StringBuilder arg) {
            if (arg.length() == 0) {
                throw new IllegalArgumentException("empty string builder");
            } else {
                stringBuilderReverserConsumer.accept(arg);
            }
        }
    };

    private static final List<StringBuilder> newBuilders() {
        final List<StringBuilder> counters = new ArrayList<StringBuilder>(2);
        counters.add(new StringBuilder("hi"));
        counters.add(new StringBuilder("jsync"));
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
        final List<StringBuilder> builders = newBuilders();

        Jsync.forEach(builders, stringBuilderReverserConsumer);

        assertEquals("ih", builders.get(0).toString());
        assertEquals("cnysj", builders.get(1).toString());
    }

    @Test
    public void itemsArrayShouldNotBeMutated() {
        final StringBuilder firstBuilder = new StringBuilder("hi");
        final StringBuilder secondBuilder = new StringBuilder("jsync");

        final StringBuilder[] builders = new StringBuilder[] { firstBuilder, secondBuilder };
        final int buildersLength = builders.length;

        Jsync.forEach(builders, stringBuilderReverserConsumer);

        assertEquals(buildersLength, builders.length);
        assertTrue(firstBuilder == builders[0]);
        assertTrue(secondBuilder == builders[1]);
    }

    @Test
    public void itemsCollectionShouldNotBeMutated() {
        final StringBuilder firstBuilder = new StringBuilder(0);
        final StringBuilder secondBuilder = new StringBuilder(1);

        final List<StringBuilder> builders = new ArrayList<StringBuilder>(2);
        builders.add(firstBuilder);
        builders.add(secondBuilder);

        final int countersSize = builders.size();

        Jsync.forEach(builders, stringBuilderReverserConsumer);

        assertEquals(countersSize, builders.size());
        assertTrue(firstBuilder == builders.get(0));
        assertTrue(secondBuilder == builders.get(1));
    }

    @Test
    public void itemsShouldBeConsumedInParallel() {
        final List<StringBuilder> builders = newBuilders();

        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.forEach(builders, oneSecondSleepConsumer);
        stopWatch.stop();

        System.out.println("2 * oneSecondSleepConsumer consumed in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }
}
