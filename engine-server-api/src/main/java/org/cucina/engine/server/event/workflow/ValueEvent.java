
package org.cucina.engine.server.event.workflow;

import org.cucina.engine.server.event.EngineEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ValueEvent
    extends EngineEvent {
    private static final long serialVersionUID = 8650842817664929311L;
    private Object value;

    /**
     * Creates a new ValueEvent object.
     *
     * @param source JAVADOC.
     */
    public ValueEvent(Object source) {
        super(source);
    }

    /**
     * Creates a new ValueEvent object.
     */
    public ValueEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param value JAVADOC.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Object getValue() {
        return value;
    }
}
