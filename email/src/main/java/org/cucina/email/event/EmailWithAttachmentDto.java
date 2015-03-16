package org.cucina.email.event;

import java.io.Serializable;

import java.util.Collection;

import javax.activation.DataSource;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.cucina.email.EmailDto;
import org.cucina.email.service.EmailUser;


/**
 * Container object to contain information required by email sender to create a
 * message and transport it to the recipients.
 *
 * @author $author$
 * @version $Revision$
 */
public class EmailWithAttachmentDto
    extends EmailDto
    implements Serializable {
    private static final long serialVersionUID = 1L;

    // TODO either decide against serialization or provide custom serialization
    // for this field
    private transient Collection<DataSource> attachments;
    private Collection<EmailUser> bccUsers;
    private Collection<EmailUser> ccUsers;
    private Collection<EmailUser> toUsers;

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
     * JAVADOC Method Level Comments
     *
     * @param bccUsers JAVADOC.
     */
    public void setBccUsers(Collection<EmailUser> bccUsers) {
        this.bccUsers = bccUsers;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<EmailUser> getBccUsers() {
        return bccUsers;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param ccUsers JAVADOC.
     */
    public void setCcUsers(Collection<EmailUser> ccUsers) {
        this.ccUsers = ccUsers;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<EmailUser> getCcUsers() {
        return ccUsers;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param toUsers JAVADOC.
     */
    public void setToUsers(Collection<EmailUser> toUsers) {
        this.toUsers = toUsers;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<EmailUser> getToUsers() {
        return toUsers;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append(attachments).toString();
    }
}
