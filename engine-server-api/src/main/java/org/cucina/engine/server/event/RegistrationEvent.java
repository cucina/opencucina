
package org.cucina.engine.server.event;

import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class RegistrationEvent
    extends EngineEvent {
    private static final long serialVersionUID = -3541843386836576243L;
    private String applicationName;
    private String destinationName;

    /**
     * Creates a new RegistrationEvent object.
     *
     * @param source
     *            JAVADOC.
     */
    public RegistrationEvent(Object source, String applicationName, String destinationName) {
        super(source);
        Assert.hasText(applicationName, "applicationName is empty");
        this.applicationName = applicationName;
        Assert.hasText(destinationName, "destinationName is empty");
        this.destinationName = destinationName;
    }

    /**
     * Creates a new RegistrationEvent object. Need this to be able to use in
     * JSON serialization.
     */
    public RegistrationEvent() {
        super("JSON");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationName
     *            JAVADOC.
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        super.source = applicationName;
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
     * @param destinationName
     *            JAVADOC.
     */
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getDestinationName() {
        return destinationName;
    }
}
