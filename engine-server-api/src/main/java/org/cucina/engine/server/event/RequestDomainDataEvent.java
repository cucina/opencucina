
package org.cucina.engine.server.event;


/**
 * Request domain data from client.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RequestDomainDataEvent
    extends GetValueEvent {
    private static final long serialVersionUID = -6262601740415980998L;
    private Long id;
    private String applicationType;

    /**
     * Creates a new RequestDomainDataEvent object.
     *
     * @param source Object.
     */
    public RequestDomainDataEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new RequestDomainDataEvent object.
     */
    public RequestDomainDataEvent() {
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

    /**
     * Set id
     *
     * @param id String.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get id
     *
     * @return id Long.
     */
    public Long getId() {
        return id;
    }
}
