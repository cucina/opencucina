package org.cucina.i18n.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.cucina.core.InstanceFactory;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.i18n.I18nMessage;
import org.cucina.i18n.repository.MessageRepository;
import org.cucina.i18n.service.I18nService;
import org.cucina.i18n.service.MessageHelper;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.springframework.util.Assert;


/**
 * Multi-internationalised message which contains base information, e.g. code,
 * basename and contains multiple internationalised messages. Provides accessor
 * methods to return message text according to a Locale.
 *
 * @author $author$
 * @version $Revision:$
 */
@Entity
@Cache(coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
public class Message
    extends PersistableEntity
    implements Comparable<Message>, Versioned {
    private static final long serialVersionUID = -1;
    private Collection<MutableI18nMessage> internationalisedMessages = new HashSet<MutableI18nMessage>();
    private transient I18nService i18nService;
    private transient InstanceFactory instanceFactory;
    private String baseName;
    private String messageCd;
    private int version;

    /**
     * Set baseName for this message.
     *
     * @param baseName
     *            String.
     */
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    /**
     * Get baseName for this message.
     *
     * @return String.
     */
    @Column(name = "base_name")
    public String getBaseName() {
        return baseName;
    }

    /**
     * Gets the best message for this Locale, if none can be found the message
     * for the locale set on {@link MessageRepository}.getDefaultLocale is returned.
     *
     * @param locale
     *            Locale.
     *
     * @return String.
     */
    public String getBestMessage(Locale locale) {
        I18nMessage message = getI18nMessage(locale);

        if (message != null) {
            return message.getMessageTx();
        }

        return (getMessageCd() == null) ? StringUtils.EMPTY : ("{" + getMessageCd() + "}");
    }

    /**
     * Returns the message if one, otherwise null
     *
     * @param locale
     * @return
     */
    public String getBestMessageOnly(Locale locale) {
        I18nMessage message = getI18nMessage(locale);

        if (message != null) {
            return message.getMessageTx();
        }

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param locale
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public I18nMessage getI18nMessage(Locale locale) {
        List<Locale> derivedLocales = MessageHelper.getDerivedLocales(locale);

        for (Locale derivedLocale : derivedLocales) {
            I18nMessage message = getI18nMessage(derivedLocale.toString());

            if (message != null) {
                return message;
            }
        }

        return null;
    }

    /**
     * Get i18nMessages. Has only been revealed for jpa, do not use this
     * method to mess about with the internationalised messages, let the message
     * do the work.
     *
     * @param messages
     *            Collection.
     */
    public void setInternationalisedMessages(Collection<MutableI18nMessage> messages) {
        internationalisedMessages = messages;
    }

    /**
     * Get i18nMessages. Has only been revealed for jpa, do not use this
     * method to mess about with the internationalised messages, let the message
     * do the work.
     *
     * @return Collection.
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "message")
    public Collection<MutableI18nMessage> getInternationalisedMessages() {
        return internationalisedMessages;
    }

    /**
     * returns an immutable message currently set for the Locale if exists, null
     * otherwise.
     *
     * @param locale
     * @return I18nMessage or null if it doesn't exist
     */
    public I18nMessage getMessage(String localeCd) {
        I18nMessage message = getI18nMessage(localeCd);

        if (message != null) {
            return new ImmutableInternationalisedMessage(message);
        }

        return null;
    }

    /**
     * Set messageCd code for this message.
     *
     * @param messageCd
     *            String.
     */
    public void setMessageCd(String messageCd) {
        this.messageCd = messageCd;
    }

    /**
     * Get messageCd code for this message.
     *
     * @return String.
     */
    @Column(name = "message_cd")
    public String getMessageCd() {
        return messageCd;
    }

    /**
     * Sets/updates messageTx for this locale. If there isn't a message for this
     * locale it creates a new one.
     *
     * @param messageTx
     *            String text for this message.
     * @param localeCd
     *            String locale for this message text.
     */
    public void setMessageTx(String messageTx, String localeCd) {
        Assert.notNull(messageTx, "messageTx cannot be null");
        Assert.notNull(localeCd, "localeCd cannot be null");

        I18nMessage message = getI18nMessage(localeCd);

        if (message == null) {
            // if not we want to create a new message and add it to the
            // collection
            MutableI18nMessage imessage = getInstanceFactory().getBean(MutableI18nMessage.TYPE);

            imessage.setLocaleCd(localeCd);
            imessage.setMessage(this);
            getInternationalisedMessages().add(imessage);
            message = imessage;
        }

        message.setMessageTx(messageTx);
    }

    /**
     * Gets messageTx if there is one for this locale.
     *
     * @return messageTx String or null if there is no message.
     */
    public String getMessageTx(String localeCd) {
        I18nMessage message = getI18nMessage(localeCd);

        if (message != null) {
            return message.getMessageTx();
        }

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param version
     *            JAVADOC.
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
     * Compares these messages according to the <code>getBestMessage</code>
     * passing in the locale of the currently logged in User.
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Message o) {
        return new CompareToBuilder().append(getBestMessage(getI18nService().getLocale()),
            o.getBestMessage(getI18nService().getLocale())).toComparison();
    }

    /**
     * Used in history comparison to check messages haven't changed
     *
     * @param message
     * @return
     */
    public boolean equalsMessages(Message message) {
        // Do the quicker wins first
        if ((message == null) ||
                (this.getInternationalisedMessages().size() != message.getInternationalisedMessages()
                                                                          .size())) {
            return false;
        }

        for (I18nMessage thisMessage : getInternationalisedMessages()) {
            I18nMessage thatMessage = message.getMessage(thisMessage.getLocaleCd());

            if ((thatMessage == null) ||
                    ((thisMessage.getMessageTx() == null) && (thatMessage.getMessageTx() != null)) ||
                    !thisMessage.getMessageTx().equals(thatMessage.getMessageTx())) {
                return false;
            }
        }

        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param localeCd
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public boolean removeMessage(String localeCd) {
        I18nMessage message = getI18nMessage(localeCd);

        if (message != null) {
            return getInternationalisedMessages().remove(message);
        }

        return false;
    }

    /**
     * implementation of toString.
     *
     * @return String.
     */
    public String toString() {
        Locale loc = null;

        if (null != getI18nService()) {
            loc = getI18nService().getLocale();
        }

        if (null == loc) {
            loc = Locale.getDefault();
        }

        return getBestMessage(loc);
    }

    /**
     * returns the message currently set for the Locale if exists, null
     * otherwise.
     *
     * @param locale
     * @return
     */
    @Transient
    private I18nMessage getI18nMessage(String localeCd) {
        for (I18nMessage message : getInternationalisedMessages()) {
            if (localeCd.equals(message.getLocaleCd())) {
                return message;
            }
        }

        return null;
    }

    /**
     * Returns the i18nService configured in I18nService
     *
     * @return instanceFactory InstanceFactory
     */
    @Transient
    private I18nService getI18nService() {
        if (i18nService == null) {
            i18nService = (I18nService) SingletonBeanFactory.getInstance()
                                                            .getBean(SingletonBeanFactory.I18N_SERVICE_ID);
        }

        return i18nService;
    }

    /**
     * Returns the instanceFactory configured in BeanFactory
     *
     * @return instanceFactory InstanceFactory
     */
    @Transient
    private InstanceFactory getInstanceFactory() {
        if (instanceFactory == null) {
            instanceFactory = (InstanceFactory) SingletonBeanFactory.getInstance()
                                                                    .getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID);
        }

        return instanceFactory;
    }

    /**
     * Immutable implementation of I18nMessage so that clients can't mess around
     * with the internationalised messages. Just delegates to the Mutable
     * version which it has set as a property.
     *
     * @author thornton
     *
     */
    private static class ImmutableInternationalisedMessage
        implements I18nMessage {
        private I18nMessage message;

        /**
         * Creates a new ImmutableInternationalisedMessage object.
         *
         * @param message
         *            JAVADOC.
         */
        public ImmutableInternationalisedMessage(I18nMessage message) {
            super();
            Assert.notNull(message, "message cannot be null");
            this.message = message;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @return JAVADOC.
         */
        public String getLocaleCd() {
            return message.getLocaleCd();
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @param arg0 JAVADOC.
         */
        @Override
        public void setMessageTx(String arg0) {
            // noop to be immutable
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @return JAVADOC.
         */
        public String getMessageTx() {
            return message.getMessageTx();
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @param obj
         *            JAVADOC.
         *
         * @return JAVADOC.
         */
        public boolean equals(Object obj) {
            if (!(obj instanceof ImmutableInternationalisedMessage)) {
                return false;
            }

            ImmutableInternationalisedMessage toCheck = (ImmutableInternationalisedMessage) obj;

            if (message.equals(toCheck.message)) {
                return true;
            }

            return false;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @return JAVADOC.
         */
        public int hashCode() {
            return message.hashCode();
        }
    }
}
