package org.cucina.i18n;

import java.text.MessageFormat;

import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.i18n.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@RestController
@RequestMapping(value = "/message")
public class MessageController {
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private MessageService messageService;

    /**
     * This is the main business method for consumers.
     *
     * @param code JAVADOC.
     * @param locale JAVADOC.
     *
     * @return JAVADOC.
     */

    //Access-Control-Allow-Origin: *
    @RequestMapping(method = RequestMethod.GET)
    public MessageFormat getMessage(String code, Locale locale, String applicationName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolve code '" + code + "' for locale=" + locale + " applicationName '" +
                applicationName + "'");
        }

        String messageTx = messageService.loadMessage(code, locale, applicationName);

        if (messageTx != null) {
            return createMessageFormat(StringUtils.replace(messageTx, "'", "''"), locale);
        }

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public Collection<MessageDto> allMessages() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("allMessages");
        }

        return messageService.loadAll();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param page JAVADOC.
     * @param size JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all/{page}/{size}")
    public Collection<MessageDto> allMessages(@PathVariable
    int page, @PathVariable
    int size) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("allMessages");
        }

        return messageService.loadAll(page, size);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public MessageDto messageDetails(@PathVariable
    Long id, @RequestHeader(required=false)
    Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("messageDetails for id=" + id + " and locale=" + locale);
        }

        return messageService.loadById(id, locale);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param text JAVADOC.
     * @param code JAVADOC.
     * @param locale JAVADOC.
     * @param application JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.POST)
    public Long saveMessage(@RequestBody
    MessageDto messageDto, @RequestHeader(required = false)
    Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("saving message: " + messageDto);
        }

        // TODO validation
        if (messageDto.getLocale() == null) {
            messageDto.setLocale(locale);
        }

        return messageService.saveMessage(messageDto);
    }

    private MessageFormat createMessageFormat(String msg, Locale locale) {
        return new MessageFormat(((msg != null) ? msg : ""), locale);
    }
}
