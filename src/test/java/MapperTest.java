import com.mbouchenoire.jsync.Function;
import com.mbouchenoire.jsync.Jsync;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mbouchenoire
 */
public class MapperTest {

    private static final Function<String, Integer> getLengthFunction = new Function<String, Integer>() {
        public Integer apply(String item) {
            return item.length();
        }
    };

    private static final Function<String, Integer> oneSecondSleepFunction = new Function<String, Integer>() {
        public Integer apply(String item) {
            try {
                Thread.sleep(1000);
                return getLengthFunction.apply(item);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsArrayShouldThrowException() {
        String[] items = null;
        Jsync.map(items, getLengthFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullItemsListShouldThrowException() {
        List<String> items = null;
        Jsync.map(items, getLengthFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullFunctionShouldThrowException() {
        Jsync.map(new String[0], null);
    }

    @Test
    public void mapperShouldMap() {
        final Integer[] lengths = Jsync.map(new String[] { "hi", "jsync" }, getLengthFunction);
        assertEquals(2, (int)lengths[0]);
        assertEquals(5, (int)lengths[1]);
    }

    @Test
    public void arrayOrCollectionShouldProduceSameResult() {
        final String[] stringsArray = new String[] { "hi", "jsync" };

        final Integer[] countsArray = Jsync.map(stringsArray, getLengthFunction);
        final List<Integer> countsList = Jsync.map(Arrays.asList(stringsArray), getLengthFunction);

        assertEquals(countsArray.length, countsList.size());

        for(int i = 0; i < countsArray.length; i++) {
            assertEquals(countsArray[i], countsList.get(i));
        }
    }

    @Test
    public void itemsShouldBeMappedInParallel() {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Jsync.map(new String[] { "hi", "jsync" }, oneSecondSleepFunction);
        stopWatch.stop();

        System.out.println("2 * oneSecondSleepFunction runned in " + stopWatch.getTime() + " millis");
        assertTrue(stopWatch.getTime() < 2000);
    }
}
