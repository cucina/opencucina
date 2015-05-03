package org.cucina.conversation.events;

import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CallbackEvent
    extends OriginatedEngineEvent {
    private static final long serialVersionUID = 1837060619272315839L;
    private Map<String, Object> parameters;

    /**
     * Creates a new CallbackEvent object.
     *
     * @param element JAVADOC.
     */
    public CallbackEvent(Object source, Map<String, Object> parameters, String applicationName) {
        super(source);
        setApplicationName(applicationName);
        this.parameters = parameters;
    }

    /**
     * Creates a new CallbackEvent object. Needed by JSON serializer.
     */
    public CallbackEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param parameters JAVADOC.
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }
}
