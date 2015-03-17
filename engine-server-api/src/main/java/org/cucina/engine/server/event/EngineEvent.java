package org.cucina.engine.server.event;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.context.ApplicationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class EngineEvent
    extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private Object serialSource;

    /**
     * Creates a new EngineEvent object.
     */
    public EngineEvent(Object source) {
        super(source);
        this.serialSource = source;
    }

    /**
     * Creates a new EngineEvent object.
     */
    public EngineEvent() {
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

        if (source == null) {
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
        return ToStringBuilder.reflectionToString(this);
    }
}
