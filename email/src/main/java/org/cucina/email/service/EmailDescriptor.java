
package org.cucina.email.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Container object to contain information required by email sender to create a
 * message and transport it to the recipients.
 *
 * @author $author$
 * @version $Revision$
 */
public class EmailDescriptor
    implements Serializable {
    private static final long serialVersionUID = 1L;

    // TODO either decide against serialization or provide custom serialization
    // for this field
    private transient Collection<DataSource> attachments;
    private Collection<Object> bccUsers;
    private Collection<Object> ccUsers;
    private Collection<Object> toUsers;
    private Map<Object, Object> parameters;
    private String messageKey;

    /**
     * Set attachments
     *
     * @param attachments
     *            Collection<DataSource>.
     */
    public void setAttachments(Collection<DataSource> attachments) {
        this.attachments = attachments;
    }

    /**
     * Get attachments
     *
     * @return attachments Collection<DataSource>.
     */
    public Collection<DataSource> getAttachments() {
        return attachments;
    }

    /**
     * JAVADOC.
     *
     * @param bccUsers
     *            JAVADOC.
     */
    public void setBccUsers(Collection<Object> bccUsers) {
        this.bccUsers = bccUsers;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public Collection<Object> getBccUsers() {
        return bccUsers;
    }

    /**
     * JAVADOC.
     *
     * @param ccUsers
     *            JAVADOC.
     */
    public void setCcUsers(Collection<Object> ccUsers) {
        this.ccUsers = ccUsers;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public Collection<Object> getCcUsers() {
        return ccUsers;
    }

    /**
     * @param messageKey
     *            pointing to the template.
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * @return Returns the messageKey.
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @param parameters
     *            Arbitrary map of parameters usually required by email
     *            template.
     */
    public void setParameters(Map<Object, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return Returns the parameters.
     */
    public Map<Object, Object> getParameters() {
        return parameters;
    }

    /**
     * JAVADOC.
     *
     * @param toUsers
     *            JAVADOC.
     */
    public void setToUsers(Collection<Object> toUsers) {
        this.toUsers = toUsers;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public Collection<Object> getToUsers() {
        return toUsers;
    }

    /**
     * JAVADOC.
     *
     * @param user
     *            JAVADOC.
     */
    public void addBccUser(Object user) {
        if (bccUsers == null) {
            bccUsers = new HashSet<Object>();
        }

        bccUsers.add(user);
    }

    /**
     * JAVADOC.
     *
     * @param user
     *            JAVADOC.
     */
    public void addCcUser(Object user) {
        if (ccUsers == null) {
            ccUsers = new HashSet<Object>();
        }

        ccUsers.add(user);
    }

    /**
     * JAVADOC.
     *
     * @param user
     *            JAVADOC.
     */
    public void addToUser(Object user) {
        if (toUsers == null) {
            toUsers = new HashSet<Object>();
        }

        toUsers.add(user);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append(messageKey).append(attachments).append(toUsers)
                                        .append(ccUsers).append(bccUsers).toString();
    }
}
