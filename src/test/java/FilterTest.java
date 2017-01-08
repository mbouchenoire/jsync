import com.mbouchenoire.jsync.Jsync;
import com.mbouchenoire.jsync.Predicate;
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
public class FilterTest {

    private static final List<Counter> counters = new ArrayList<Counter>(2);

    private static final Predicate<Counter> filterPositive = new Predicate<Counter>() {
        public boolean test(Counter counter) {
            return counter.getCount() >= 0;
        }
    };

    private static final Predicate<Counter> oneSecondSleepPredicate = new Predicate<Counter>() {
        public boolean test(Counter counter) {
            try {
                Thread.sleep(1000);
                return filterPositive.test(counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
    };

    static {
        counters.add(new Counter(-5));
        counters.add(new Counter(-1));
        counters.add(new Counter(2));
        counters.add(new Counter(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsArrayShouldThrowException() {
        Counter[] items = null;
        Jsync.filter(items, filterPositive);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsListShouldThrowException() {
        List<Counter> items = null;
        Jsync.filter(items, filterPositive);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPredicateShouldThrowException() {
        Jsync.filter(counters, null);
    }

    @Test
    public void filterShouldFilter() {
        final Collection<Counter> positiveCounters = Jsync.filter(counters, filterPositive);
        final List<Counter> countersList = new ArrayList<Counter>(positiveCounters);

        assertEquals(2, countersList.size());

        assertEquals(2, countersList.get(0).getCount());
        assertEquals(4, countersList.get(1).getCount());
    }

    @Test
    public void arrayOrCollectionShouldProduceSameResult() {
        final Counter[] countersArray = new Counter[counters.size()];

        final Counter[] filteredCountersArray = Jsync.filter(counters.toArray(countersArray), filterPositive);
        final Collection<Counter> filteredCountersCollection = Jsync.filter(counters, filterPositive);
        final List<Counter> filteredCountersList = new ArrayList<Counter>(filteredCountersCollection);

        for(int i = 0; i < filteredCountersArray.length; i++) {
            assertEquals(filteredCountersArray[i].getCount(), filteredCountersList.get(i).getCount());
        }
    }

    @Test
    public void itemsShouldBeFilteredInParallel() {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.filter(counters, oneSecondSleepPredicate);
        stopWatch.stop();

        System.out.println("4 * oneSecondSleepPredicate runned in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }
}
