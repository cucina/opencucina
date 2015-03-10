package org.cucina.i18n;

import java.text.MessageFormat;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.i18n.service.MessageLoader;

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
    private MessageLoader messageLoader;

    /**
     * JAVADOC Method Level Comments
     *
     * @param code JAVADOC.
     * @param locale JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET)
    public MessageFormat getMessage(String code, Locale locale, String applicationName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolve code '" + code + "' for locale=" + locale);
        }

        String messageTx = messageLoader.loadMessage(code, locale, applicationName);

        if (messageTx != null) {
            return createMessageFormat(StringUtils.replace(messageTx, "'", "''"), locale);
        }

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param text JAVADOC.
     * @param code JAVADOC.
     * @param locale JAVADOC.
     * @param applicationName JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.POST)
    public boolean saveMessage(String text, String code, Locale locale, String applicationName) {
        return messageLoader.saveMessage(text, code, locale, applicationName);
    }

    private MessageFormat createMessageFormat(String msg, Locale locale) {
        return new MessageFormat(((msg != null) ? msg : ""), locale);
    }
}
