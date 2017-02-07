import com.mbouchenoire.jsync.Jsync;
import org.junit.Test;

import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;

/**
 * @author mbouchenoire
 */
public final class JsyncTest {

    @Test
    public void buildShouldReturnBuilder() {
        assertNotNull(Jsync.builder(Executors.newSingleThreadExecutor()));
    }
}
