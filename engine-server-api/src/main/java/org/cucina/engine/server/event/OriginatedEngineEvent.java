
package org.cucina.engine.server.event;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class OriginatedEngineEvent
    extends EngineEvent {
    private static final long serialVersionUID = -3806305660477926L;
    private String applicationName;
    private String conversationId;

    /**
    * Creates a new OriginatedEvent object.
    *
    * @param source JAVADOC.
    * @param applicationName JAVADOC.
    */
    public OriginatedEngineEvent(Object source) {
        super(source);
    }

    /**
     * Creates a new OriginatedEngineEvent object. Need this for JSON serialization.
     */
    public OriginatedEngineEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationName JAVADOC.
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     */
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getConversationId() {
        return conversationId;
    }
}