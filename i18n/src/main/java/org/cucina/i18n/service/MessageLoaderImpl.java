package org.cucina.i18n.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.StringValueTransformer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.cucina.core.InstanceFactory;

import org.cucina.i18n.I18nMessage;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The class performs couple of useful operations during messages search. First,
 * it builds a list of locales starting from the provided one, and generalizing
 * by removing variant and country. Also adds to the bottom of the list default
 * locale with generalized locales added to it too. So the list for the locale
 * <code>es_ES_traditional</code> with platform default locale
 * <code>en_UK</code>would look like this:
 * <ol>
 * <li>es_ES_traditional
 * <li>es_ES
 * <li>es
 * <li>en_UK
 * <li>en
 * </ol>
 *
 * Second, messages ordered by locale and basenames in order to find the nearest
 * match in that order.
 *
 *
 * @author $author$
 * @version $Revision: 1.7 $
 */
@Service
public class MessageLoaderImpl
    implements MessageLoader {
    private static final Logger LOG = LoggerFactory.getLogger(MessageLoaderImpl.class);
    private InstanceFactory instanceFactory;
    private MessageRepository messageRepository;

    /**
     * Creates a new MessageLoaderImpl object.
     *
     * @param instanceFactory
     *            JAVADOC.
     * @param messageRepository
     *            JAVADOC.
     */
    @Autowired
    public MessageLoaderImpl(InstanceFactory instanceFactory, MessageRepository messageRepository) {
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
        Assert.notNull(messageRepository, "messageRepository is null");
        this.messageRepository = messageRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param code
     *            JAVADOC.
     * @param locale
     *            JAVADOC.
     * @param basenames
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see com.ibm.algo.zest.message.MessageLoader#loadMessage(java.lang.String,
     *      java.util.Locale, java.util.List)
     */
    @Transactional
    public String loadMessage(String code, Locale locale, String... basenames) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        // Great list of locales including default locales as a fall back
        List<Locale> locales = MessageHelper.getDerivedLocales(locale);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading for code '" + code + "' for locales=" + locales + " and basenames=" +
                basenames);
        }

        List<String> localesNames = new ArrayList<String>();

        CollectionUtils.collect(locales, StringValueTransformer.getInstance(), localesNames);

        Map<String, Map<String, I18nMessage>> orderedByLocale = new HashMap<String, Map<String, I18nMessage>>();
        Collection<Message> messages = null;

        if (ArrayUtils.isNotEmpty(basenames)) {
            messages = messageRepository.findByBasenamesAndCode(Arrays.asList(basenames), code);
        } else {
            messages = messageRepository.findByCode(code);
        }

        for (Message message : messages) {
            for (String localeName : localesNames) {
                I18nMessage i18nMessage = message.getMessage(localeName);

                if (i18nMessage != null) {
                    Map<String, I18nMessage> orderedByBase = orderedByLocale.get(localeName);

                    if (orderedByBase == null) {
                        orderedByBase = new HashMap<String, I18nMessage>();
                    }

                    if (ArrayUtils.isEmpty(basenames)) {
                        orderedByBase.put("", i18nMessage);
                    } else {
                        orderedByBase.put(message.getBaseName(), i18nMessage);
                    }

                    orderedByLocale.put(localeName, orderedByBase);
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("messages ordered by locale:" + orderedByLocale);
        }

        // get the best match, considering locales first
        for (String localeCd : localesNames) {
            Map<String, I18nMessage> messagesMap = orderedByLocale.get(localeCd);

            if (messagesMap != null) {
                if (basenames != null) {
                    for (String bn : basenames) {
                        I18nMessage message = messagesMap.get(bn);

                        if (message != null) {
                            return message.getMessageTx();
                        }
                    }
                } else {
                    I18nMessage message = messagesMap.get("");

                    if (message != null) {
                        return message.getMessageTx();
                    }
                }
            }
        }

        return null;
    }

    @Override
    @Transactional
    public boolean saveMessage(String text, String code, Locale locale, String applicationName) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        Message message = messageRepository.findByBasenameAndCode(applicationName, code);

        if (message == null) {
            message = instanceFactory.getBean(Message.class.getSimpleName());
            message.setBaseName(StringUtils.isEmpty(applicationName) ? null : applicationName);
            message.setMessageCd(code);
        }

        message.setMessageTx(text, locale.toString());

        messageRepository.save(message);

        return true;
    }
}
