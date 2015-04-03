package org.cucina.core.validation;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.apache.commons.lang3.StringUtils;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.util.Assert;

import org.cucina.core.service.ContextService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Does not work with Hibernate Validation implementation - is never called.
 *
 * @deprecated
 */
public class ContextServiceMessageInterpolator
    implements MessageInterpolator, MessageSourceAware {
    private static final Logger LOG = LoggerFactory.getLogger(ContextServiceMessageInterpolator.class);
    private ContextService contextService;
    private MessageSource messageSource;

    /**
     * Creates a new ContextServiceMessageInterpolator object.
     *
     * @param contextService
     *            JAVADOC.
     */
    public ContextServiceMessageInterpolator(ContextService contextService) {
        Assert.notNull(contextService, "contextService is null");
        this.contextService = contextService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageSource
     *            JAVADOC.
     */
    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageTemplate
     *            JAVADOC.
     * @param context
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageTemplate
     *            JAVADOC.
     * @param context
     *            JAVADOC.
     * @param locale
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Interpolating '" + messageTemplate + "' for locale " + locale);
        }

        String message = (String) contextService.get(messageTemplate);

        if (StringUtils.isEmpty(message)) {
            return messageSource.getMessage(messageTemplate, new Object[] {  }, locale);
        }

        return messageSource.getMessage(message, new Object[] {  }, locale);
    }
}
