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

    private static final List<String> strings = new ArrayList<String>(2);

    private static final Predicate<String> filterNotNullOrEmpty = new Predicate<String>() {
        public boolean test(String arg) {
            return (arg != null && arg.length() > 0);
        }
    };

    private static final Predicate<String> oneSecondSleepPredicate = new Predicate<String>() {
        public boolean test(String arg) {
            try {
                Thread.sleep(1000);
                return filterNotNullOrEmpty.test(arg);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
    };

    static {
        strings.add("hi");
        strings.add(null);
        strings.add("jsync");
        strings.add("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsArrayShouldThrowException() {
        String[] strings = null;
        Jsync.filter(strings, filterNotNullOrEmpty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsListShouldThrowException() {
        List<String> strings = null;
        Jsync.filter(strings, filterNotNullOrEmpty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPredicateShouldThrowException() {
        Jsync.filter(strings, null);
    }

    @Test
    public void filterShouldFilter() {
        final Collection<String> notNullOrEmptyStrings = Jsync.filter(strings, filterNotNullOrEmpty);
        final List<String> stringsList = new ArrayList<String>(notNullOrEmptyStrings);

        assertEquals(2, stringsList.size());

        assertEquals("hi", stringsList.get(0));
        assertEquals("jsync", stringsList.get(1));
    }

    @Test
    public void arrayOrCollectionShouldProduceSameResult() {
        final String[] countersArray = new String[strings.size()];

        final String[] filteredStringsArray = Jsync.filter(strings.toArray(countersArray), filterNotNullOrEmpty);
        final Collection<String> filteredStringsCollection = Jsync.filter(strings, filterNotNullOrEmpty);
        final List<String> filteredStringsList = new ArrayList<String>(filteredStringsCollection);

        for(int i = 0; i < filteredStringsArray.length; i++) {
            assertEquals(filteredStringsArray[i], filteredStringsList.get(i));
        }
    }

    @Test
    public void itemsShouldBeFilteredInParallel() {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.filter(strings, oneSecondSleepPredicate);
        stopWatch.stop();

        System.out.println("4 * oneSecondSleepPredicate runned in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }
}
