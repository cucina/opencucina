package org.cucina.i18n.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.StringValueTransformer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.cucina.i18n.MessageDto;
import org.cucina.i18n.model.I18nMessage;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


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
public class MessageServiceImpl
    implements MessageService {
    private static final Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);
    private ConversionService conversionService;
    private I18nService i18nService;
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
    public MessageServiceImpl(I18nService i18nService, MessageRepository messageRepository,
        @Qualifier(value = "myConversionService")
    ConversionService conversionService) {
        Assert.notNull(i18nService, "i18nService is null");
        this.i18nService = i18nService;
        Assert.notNull(messageRepository, "messageRepository is null");
        this.messageRepository = messageRepository;
        Assert.notNull(conversionService, "conversionService is null");
        this.conversionService = conversionService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<MessageDto> loadAll() {
        Collection<Message> messages = messageRepository.findAll();
        Collection<MessageDto> result = new LinkedList<MessageDto>();

        for (Message message : messages) {
            @SuppressWarnings("unchecked")
            Collection<MessageDto> dtos = conversionService.convert(message, Collection.class);

            if (dtos != null) {
                result.addAll(dtos);
            }
        }

        return result;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param page
     *            JAVADOC.
     * @param size
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<MessageDto> loadAll(int page, int size) {
        Pageable pageable = new PageRequest(page, size);
        Collection<Message> messages = messageRepository.findAll(pageable);
        Collection<MessageDto> result = new LinkedList<MessageDto>();

        for (Message message : messages) {
            @SuppressWarnings("unchecked")
            Collection<MessageDto> dtos = conversionService.convert(message, Collection.class);

            if (dtos != null) {
                result.addAll(dtos);
            }
        }

        return result;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message loadById(Long id) {
        return messageRepository.findById(id);
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
     * @see com.MessageService.algo.zest.message.MessageLoader#loadMessage(java.lang.String,
     *      java.util.Locale, java.util.List)
     */
    @Transactional
    public String loadMessage(String code, Locale locale, String... basenames) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        // Create list of locales including default locale as a fall back
        List<Locale> locales = LocaleUtils.localeLookupList(locale,
                messageRepository.getDefaultLocale());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading for code '" + code + "' for locales=" + locales + " and basenames=" +
                Arrays.toString(basenames));
        }

        List<String> localesNames = new ArrayList<String>();

        CollectionUtils.collect(locales, StringValueTransformer.getInstance(), localesNames);

        Collection<Message> messages = null;

        if (ArrayUtils.isNotEmpty(basenames)) {
            messages = messageRepository.findByBasenamesAndCode(Arrays.asList(basenames), code);
        } else {
            messages = messageRepository.findByCode(code);
        }

        Map<String, Map<String, I18nMessage>> orderedByLocale = new HashMap<String, Map<String, I18nMessage>>();

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
    public boolean saveMessage(MessageDto messageDto) {
        Locale locale = messageDto.getLocale();

        if (messageDto.getLocale() == null) {
            locale = i18nService.getLocale();
        }

        try {
            messageRepository.save(messageDto.getApplication(), locale.toString(),
                messageDto.getCode(), messageDto.getText());

            return true;
        } catch (Exception e) {
            LOG.error("Oops", e);

            return false;
        }
    }
}
