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

    private static final Consumer<StringBuffer> oneSecondSleepConsumer = new Consumer<StringBuffer>() {
        public void accept(StringBuffer arg) {
            try {
                Thread.sleep(1000);
                printConsumer.accept(arg);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    };

    private static final Consumer<StringBuffer> stringBufferReverserConsumer = new Consumer<StringBuffer>() {
        public void accept(StringBuffer arg) {
            arg.reverse();
        }
    };

    private static final List<StringBuffer> newBuilders() {
        final List<StringBuffer> buffers = new ArrayList<StringBuffer>(2);
        buffers.add(new StringBuffer("hi"));
        buffers.add(new StringBuffer("jsync"));
        return buffers;
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
        final List<StringBuffer> buffers = newBuilders();

        Jsync.forEach(buffers, stringBufferReverserConsumer);

        assertEquals("ih", buffers.get(0).toString());
        assertEquals("cnysj", buffers.get(1).toString());
    }

    @Test
    public void itemsArrayShouldNotBeMutated() {
        final StringBuffer firstBuffer = new StringBuffer("hi");
        final StringBuffer secondBuffer = new StringBuffer("jsync");

        final StringBuffer[] buffers = new StringBuffer[] { firstBuffer, secondBuffer };
        final int buildersLength = buffers.length;

        Jsync.forEach(buffers, stringBufferReverserConsumer);

        assertEquals(buildersLength, buffers.length);
        assertTrue(firstBuffer == buffers[0]);
        assertTrue(secondBuffer == buffers[1]);
    }

    @Test
    public void itemsCollectionShouldNotBeMutated() {
        final StringBuffer firstBuffer = new StringBuffer(0);
        final StringBuffer secondBuffer = new StringBuffer(1);

        final List<StringBuffer> buffers = new ArrayList<StringBuffer>(2);
        buffers.add(firstBuffer);
        buffers.add(secondBuffer);

        final int countersSize = buffers.size();

        Jsync.forEach(buffers, stringBufferReverserConsumer);

        assertEquals(countersSize, buffers.size());
        assertTrue(firstBuffer == buffers.get(0));
        assertTrue(secondBuffer == buffers.get(1));
    }

    @Test
    public void itemsShouldBeConsumedInParallel() {
        final List<StringBuffer> buffers = newBuilders();

        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.forEach(buffers, oneSecondSleepConsumer);
        stopWatch.stop();

        System.out.println("2 * oneSecondSleepConsumer consumed in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }
}
