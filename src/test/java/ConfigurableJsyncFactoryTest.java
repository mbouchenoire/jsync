import com.mbouchenoire.jsync.ConfigurableJsyncFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 *  @author mbouchenoire
 */
public final class ConfigurableJsyncFactoryTest {

    @Test
    public void factoryMethodShouldProduceInstance() {
        assertNotNull(ConfigurableJsyncFactory.createDefault());
    }
}
