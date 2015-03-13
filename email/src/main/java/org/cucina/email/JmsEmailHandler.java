package org.cucina.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.cucina.email.service.EmailService;
import org.cucina.email.service.EmailUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handles JMS text message with body as a JSON document. Following are the
 * properties:
 * <ul>
 * <li>'messageKey' - name of the template to use</li>
 * <li>'to' - CSV list of addresses</li>
 * <li>'cc' - CSV list of addresses</li>
 * <li>'bcc' - CSV list of addresses</li>
 * <li>'locale' - optional String representation of a locale in which the
 * message is preferred to be</li>
 * </ul>
 *
 * @author vlevine $
 */
@Component
public class JmsEmailHandler {
    private static final Logger LOG = LoggerFactory.getLogger(JmsEmailHandler.class);
    @Autowired
    private EmailService emailService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * endpoint method
     *
     * @param message
     *            JAVADOC.
     */
    @JmsListener(destination = "cucina.email", containerFactory="myJmsListenerContainerFactory")
    public void processMessage(Message message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received:" + message);
        }

        try {
            String body = ((TextMessage) message).getText();
            JsonNode jnode = objectMapper.readTree(body);

            String messageKey = jnode.get("messageKey").asText();
            String to = jnode.get("to").asText();
            String cc = jnode.get("cc").asText();
            String bcc = jnode.get("bcc").asText();
            String locale = jnode.get("locale").asText();

            emailService.sendMessages(messageKey, buildUsers(to, locale), buildUsers(cc, locale),
                buildUsers(bcc, locale), new HashMap<Object, Object>(), null);
        } catch (Exception e) {
            LOG.error("Oops", e);
        }
    }

    private Collection<EmailUser> buildUsers(String addresses, String locale) {
        Collection<EmailUser> users = new ArrayList<EmailUser>();

        if (StringUtils.isEmpty(addresses)) {
            return users;
        }

        String[] ass = addresses.split(",");

        for (int i = 0; i < ass.length; i++) {
            if (StringUtils.isEmpty(ass[i])) {
                continue;
            }

            users.add(new EmailUserImpl(ass[i], locale));
        }

        return users;
    }

    private class EmailUserImpl
        implements EmailUser {
        private Locale locale;
        private String email;

        public EmailUserImpl(String email, String slocale) {
            this.email = email;

            try {
                locale = LocaleUtils.toLocale(slocale);
            } catch (Exception e) {
                LOG.error("Oops", e);
            }

            if (locale == null) {
                locale = Locale.getDefault();
            }
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public Locale getLocale() {
            return locale;
        }

        @Override
        public String getUsername() {
            return null;
        }
    }
}
