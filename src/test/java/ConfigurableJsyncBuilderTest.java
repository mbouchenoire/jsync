import com.mbouchenoire.jsync.ConfigurableJsyncBuilder;
import org.junit.Test;

import java.util.concurrent.Executors;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * @author mbouchenoire
 */
public final class ConfigurableJsyncBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullExecutorServiceCtorArgumentShouldThrowException() {
        new ConfigurableJsyncBuilder(null);
        assertTrue(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullExecutorServiceMutatorShouldThrowException() {
        new ConfigurableJsyncBuilder(Executors.newSingleThreadExecutor()).executorService(null);
    }

    @Test
    public void mutatorsShouldReturnBuilder() {
        final ConfigurableJsyncBuilder builder = new ConfigurableJsyncBuilder(Executors.newSingleThreadExecutor());
        assertTrue(builder == builder.executorService(Executors.newSingleThreadExecutor()));
        assertTrue(builder == builder.timeout(0));
    }

    @Test
    public void buildShouldReturnConfigurableJsyncInstance() {
        assertNotNull(new ConfigurableJsyncBuilder(Executors.newSingleThreadExecutor()).timeout(1).build());
    }
}
