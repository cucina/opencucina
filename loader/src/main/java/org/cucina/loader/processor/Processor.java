
package org.cucina.loader.processor;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface Processor {
    /**
     * JAVADOC Method Level Comments
     *
     * @param object JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * JAVADOC Method Level Comments
     *
     * @param object JAVADOC.
     *
     * @return collection of ids of newly created objects if any or null.
     */
    Collection<Long> process(Object object);
}
