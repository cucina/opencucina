package org.cucina.i18n.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
@Entity(name = MutableI18nMessage.TABLE)
@Cacheable
public class MutableI18nMessage
    extends PersistableEntity
    implements Versioned, I18nMessage {
    private static final long serialVersionUID = 7566394708380189018L;

    /** This is a field JAVADOC */
    public static final String TABLE = "I_MESSAGE";

    /** message */
    public static final String MESSAGE_PROPERTY_NAME = "message";

    /** localeCd */
    public static final String LOCALE_PROPERTY_NAME = "localeCd";

    /** messageTx */
    public static final String MESSAGE_TX_PROPERTY_NAME = "messageTx";

    /**
    * InternationalisedMessage.
    */
    public static final String TYPE = "MutableI18nMessage";
    private Message message;
    private String localeCd;
    private String messageTx;
    private int version;

    /**
    * JAVADOC.
    *
    * @param localeCd JAVADOC.
    */
    public void setLocaleCd(String localeCd) {
        this.localeCd = localeCd;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    @Column(name = "locale_cd")
    public String getLocaleCd() {
        return localeCd;
    }

    /**
     * JAVADOC.
     *
     * @param message JAVADOC.
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "message")
    @JsonIgnore
    public Message getMessage() {
        return message;
    }

    /**
     * JAVADOC.
     *
     * @param messageTx JAVADOC.
     */
    public void setMessageTx(String messageTx) {
        this.messageTx = messageTx;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    @Column(name = "message_tx", length = 4000)
    public String getMessageTx() {
        return messageTx;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param version JAVADOC.
     */
    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @Version
    public int getVersion() {
        return version;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param obj JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MutableI18nMessage)) {
            return false;
        }

        MutableI18nMessage rhs = (MutableI18nMessage) obj;

        EqualsBuilder eqB = new EqualsBuilder().append(getLocaleCd(), rhs.getLocaleCd());

        if ((this.getMessage() != null) && (rhs.getMessage() != null)) {
            eqB.append(getMessage().getId(), rhs.getMessage().getId());
        }

        return eqB.isEquals();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public int hashCode() {
        HashCodeBuilder hcB = new HashCodeBuilder(17, 37).append(getLocaleCd());

        if (getMessage() != null) {
            hcB.append(getMessage().getId());
        }

        return hcB.toHashCode();
    }

    /**
     * Overriden toString implementation.
     *
     * @return String description of object.
     */
    public String toString() {
        return new ToStringBuilder(this).append("localeCd", getLocaleCd())
                                        .append("messageTx", getMessageTx()).toString();
    }
}
