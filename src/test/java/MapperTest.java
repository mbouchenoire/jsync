import com.mbouchenoire.jsync.Function;
import com.mbouchenoire.jsync.Jsync;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mbouchenoire
 */
public class MapperTest {

    private static final List<Counter> counters = new ArrayList<Counter>(2);

    private static final Function<Counter, Integer> getCountFunction = new Function<Counter, Integer>() {
        public Integer apply(Counter item) {
            return item.getCount();
        }
    };

    private static final Function<Counter, Integer> oneSecondSleepFunction = new Function<Counter, Integer>() {
        public Integer apply(Counter item) {
            try {
                Thread.sleep(1000);
                return getCountFunction.apply(item);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    static {
        counters.add(new Counter(0));
        counters.add(new Counter(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsArrayShouldThrowException() {
        Counter[] items = null;
        Jsync.map(items, getCountFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsListShouldThrowException() {
        List<Counter> items = null;
        Jsync.map(items, getCountFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullFunctionShouldThrowException() {
        Jsync.map(counters, null);
    }

    @Test
    public void mapperShouldMap() {
        final List<Integer> counts = Jsync.map(counters, getCountFunction);
        assertEquals(0, (int)counts.get(0));
        assertEquals(1, (int)counts.get(1));
    }

    @Test
    public void arrayOrCollectionShouldProduceSameResult() {
        final Counter[] countersArray = new Counter[counters.size()];

        final Integer[] countsArray = Jsync.map(counters.toArray(countersArray), getCountFunction);
        final List<Integer> countsList = Jsync.map(counters, getCountFunction);

        for(int i = 0; i < countersArray.length; i++) {
            assertEquals(countsArray[i], countsList.get(i));
        }
    }

    @Test
    public void itemsShouldBeMappedInParallel() {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.map(counters, oneSecondSleepFunction);
        stopWatch.stop();

        System.out.println("2 * oneSecondSleepFunction runned in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }
}
