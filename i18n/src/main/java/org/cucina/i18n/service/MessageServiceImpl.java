package org.cucina.i18n.service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.LocaleUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.cucina.i18n.MessageDto;
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
public class MessageServiceImpl
    implements MessageService {
    private static final Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);
    private ConversionService conversionService;
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
    public MessageServiceImpl(MessageRepository messageRepository,
        @Qualifier(value = "myConversionService")
    ConversionService conversionService) {
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
     * Finds the best message for the given id, assuming that Locale is in contextService.
     *
     * @param id
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public MessageDto loadById(Long id, Locale locale) {
        Assert.notNull(id, "id cannot be null");

        Message message = messageRepository.findById(id);

        if (message == null) {
            return null;
        }

        return findBestFit(message, locale);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param code
     *            JAVADOC.
     * @param locale
     *            JAVADOC.
     * @param basename
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see com.MessageService.algo.zest.message.MessageLoader#loadMessage(java.lang.String,
     *      java.util.Locale, java.util.List)
     */
    @Override
    public String loadMessage(String code, Locale locale, String basename) {
        Assert.hasText(code, "code is empty");
        Assert.hasText(code, "basename is empty");

        if (locale == null) {
            locale = Locale.getDefault();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading for code '" + code + "' for locale=" + locale + " and basename=" +
                basename);
        }

        Message message = messageRepository.findByBasenameAndCode(basename, code);

        MessageDto dto = findBestFit(message, locale);

        return (dto == null) ? null : dto.getText();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageDto JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional
    public Long saveMessage(MessageDto messageDto) {
        Locale locale = messageDto.getLocale();

        if (messageDto.getLocale() == null) {
            locale = messageRepository.getDefaultLocale();
        }

        try {
            Message m = messageRepository.save(messageDto.getApplication(), locale.toString(),
                    messageDto.getCode(), messageDto.getText());

            return m.getId();
        } catch (Exception e) {
            LOG.error("Oops", e);

            return null;
        }
    }

    private MessageDto findBestFit(Message message, Locale locale) {
        @SuppressWarnings("unchecked")
        Collection<MessageDto> mdtos = conversionService.convert(message, Collection.class);

        List<Locale> locales = (locale == null)
            ? Collections.singletonList(messageRepository.getDefaultLocale())
            : LocaleUtils.localeLookupList(locale, messageRepository.getDefaultLocale());

        for (final Locale loc : locales) {
            MessageDto dto = (MessageDto) CollectionUtils.find(mdtos,
                    new Predicate() {
                        @Override
                        public boolean evaluate(Object arg0) {
                            MessageDto dto = (MessageDto) arg0;

                            return dto.getLocale().equals(loc);
                        }
                    });

            if (dto != null) {
                return dto;
            }
        }

        return null;
    }
}
