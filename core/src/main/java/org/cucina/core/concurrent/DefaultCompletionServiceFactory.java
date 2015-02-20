
package org.cucina.core.concurrent;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DefaultCompletionServiceFactory
    implements CompletionServiceFactory {
    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param executor JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public <T> CompletionService<T> create(Executor executor) {
        // TODO pooling, caching, queueing for the same executorService
        return new ExecutorCompletionService<T>(executor);
    }
}
