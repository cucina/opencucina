
package org.cucina.engine.server.event;

import org.cucina.engine.server.event.workflow.GetValueEvent;


/**
 * Request domain data from client.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RequestDomainConfigEvent
    extends GetValueEvent {
    private static final long serialVersionUID = 8736257067208967786L;
    private String applicationType;

    /**
     * Creates a new RequestDomainDataEvent object.
     *
     * @param source Object.
     */
    public RequestDomainConfigEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new RequestDomainConfigEvent object.
     */
    public RequestDomainConfigEvent() {
    }

    /**
     * Set applicationType
     *
     * @param applicationType String.
     */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    /**
     * Get applicationType
     *
     * @return applicationType String.
     */
    public String getApplicationType() {
        return applicationType;
    }
}
