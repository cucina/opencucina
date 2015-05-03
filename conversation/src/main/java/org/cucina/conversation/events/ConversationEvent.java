package org.cucina.conversation.events;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class ConversationEvent
    extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private Object serialSource;

    /**
     * Creates a new EngineEvent object.
     */
    public ConversationEvent(Object source) {
        super(source);
        this.serialSource = source;
    }

    /**
     * Creates a new EngineEvent object.
     */
    public ConversationEvent() {
        super("JSON");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param source
     *            JAVADOC.
     */
    public void setSource(Object source) {
        this.serialSource = source;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Object getSource() {
        Object source = super.getSource();

        if (source == null || "JSON".equals(source)) {
            return serialSource;
        }

        return source;
    }

    /**
     * Default toString implementation
     *
     * @return This object as String.
     */
    @Override
    public String toString() {
    	ToStringBuilder tob = new ToStringBuilder(this);

        return tob.append("source", getSource()).toString();
    }
}
