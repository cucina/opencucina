package org.cucina.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.core.model.Message;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.repository.MessageRepository;
import org.cucina.core.spring.SingletonBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniqueMessageCodeValidator
    implements ConstraintValidator<UniqueMessageCode, PersistableEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(UniqueMessageCodeValidator.class);
    private MessageRepository messageRepository;
    private UniqueMessageCode uniqueMessageCode;

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageDao JAVADOC.
     */
    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0 JAVADOC.
     * @param arg1 JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isValid(PersistableEntity arg0, ConstraintValidatorContext arg1) {
        if (arg0 == null) {
            LOG.info("Validating null message, returning false ");

            return false;
        }

        String propertyName = uniqueMessageCode.property();
        String basename = uniqueMessageCode.basename();

        if (StringUtils.isEmpty(propertyName) || StringUtils.isEmpty(basename)) {
            LOG.warn("Validator misconfigured check propertyName and basename, returning false ");

            return false;
        }

        Object messageObj;

        try {
            messageObj = PropertyUtils.getProperty(arg0, propertyName);
        } catch (Exception e) {
            LOG.warn("Exception on getting message property with name [" + propertyName + "]", e);

            return false;
        }

        if (messageObj == null) {
            LOG.info("Message object is null, returning false");

            return false;
        }

        if (!(messageObj instanceof Message)) {
            LOG.warn("Misconfiguration, propertyname does not point to object of Message type");

            return false;
        }

        Message message = (Message) messageObj;

        String msgCd = message.getMessageCd();

        if (StringUtils.isBlank(msgCd)) {
            LOG.info("Validating message with blank message code, returning false ");

            return false;
        }

        return null == getMessageRepository()
                           .findByBasenameAndCode(uniqueMessageCode.basename(), msgCd);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param uniqueMessageCode JAVADOC.
     */
    @Override
    public void initialize(UniqueMessageCode uniqueMessageCode) {
        this.uniqueMessageCode = uniqueMessageCode;
    }

    private MessageRepository getMessageRepository() {
        if (null == messageRepository) {
            LOG.debug("Failed to autowire, attempting to hotwire byName");
            messageRepository = (MessageRepository) SingletonBeanFactory.getInstance()
                                                                        .getBean(SingletonBeanFactory.MESSAGE_REPOSITORY_ID);

            if (messageRepository == null) {
                // this may not bring back the desired result
                LOG.debug("Failed to autowire, attempting to hotwire by class");

                messageRepository = SingletonBeanFactory.getInstance()
                                                        .getBean(MessageRepository.class);
            }

            Assert.notNull(messageRepository, "messageRepository is null");
        }

        return messageRepository;
    }
}
