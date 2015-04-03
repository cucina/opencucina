
package org.cucina.loader.concurrent;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface CompletionServiceFactory {
    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param executor JAVADOC.
     *
     * @return JAVADOC.
     */
    <T> CompletionService<T> create(Executor executor);
}
