import com.mbouchenoire.jsync.ConsumerRunnableAdapter;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * @author mbouchenoire
 */
public final class ConsumerRunnableAdapterTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullConsumerShouldThrowException() {
        new ConsumerRunnableAdapter<Object>(null, new Object());
        assertTrue(false);
    }
}
