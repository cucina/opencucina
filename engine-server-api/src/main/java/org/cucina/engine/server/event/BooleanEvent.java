package org.cucina.engine.server.event;

import org.cucina.conversation.events.ConversationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class BooleanEvent
    extends ConversationEvent {
    private static final long serialVersionUID = -4576181250796688902L;
    private boolean result;

    /**
    * Creates a new BooleanEvent object.
    *
    * @param source JAVADOC.
    * @param result JAVADOC.
    */
    public BooleanEvent(Object source, boolean result) {
        super(source);
        this.result = result;
    }

    /**
     * Creates a new BooleanEvent object. Needed for JSON.
     */
    public BooleanEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param result JAVADOC.
     */
    public void setResult(boolean result) {
        this.result = result;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public boolean isResult() {
        return result;
    }
}
