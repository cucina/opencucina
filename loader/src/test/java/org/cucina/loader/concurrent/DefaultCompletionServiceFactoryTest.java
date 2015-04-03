
package org.cucina.loader.concurrent;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;

import org.cucina.loader.concurrent.DefaultCompletionServiceFactory;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DefaultCompletionServiceFactoryTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testCreate() {
        DefaultCompletionServiceFactory factory = new DefaultCompletionServiceFactory();
        Executor executor = mock(Executor.class);
        CompletionService<Object> cs = factory.create(executor);

        assertNotNull("CompletionService is null", cs);
    }
}
