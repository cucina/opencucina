
package org.cucina.core.marshal;

import com.fasterxml.jackson.core.type.TypeReference;


/**
 * Marshall objects to String representation and back again.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface Marshaller {
    /**
     * Marshall object graph to String representation
     *
     * @param graph JAVADOC.
     *
     * @return JAVADOC.
     */
    String marshall(Object graph);

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param source JAVADOC.
     * @param targetClass JAVADOC.
     *
     * @return JAVADOC.
     */
    <T> T unmarshall(String source, Class<T> targetClass);

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param source JAVADOC.
     * @param valueTypeRef JAVADOC.
     *
     * @return JAVADOC.
     */
    <T> T unmarshall(String source, TypeReference<T> valueTypeRef);
}
