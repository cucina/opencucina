package org.cucina.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.cucina.core.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoaderExceptionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(LoaderExceptionFactory.class);
    private static final String LOADER_FAILURE_SEE_ERRORS = "Loader failure (see errors)";
    private I18nService i18nService;
    private MessageSource messageSource;

    /**
     * Creates a new LoaderExceptionFactory object.
     *
     * @param i18nService JAVADOC.
     * @param messageSource JAVADOC.
     */
    public LoaderExceptionFactory(I18nService i18nService, MessageSource messageSource) {
        Assert.notNull(i18nService, "i18nService is null");
        this.i18nService = i18nService;
        this.messageSource = messageSource;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param be JAVADOC.
     *
     * @return JAVADOC.
     */
    public LoaderException getLoaderException(BindException be) {
        String[] errors = getI18nErrors(i18nService.getLocale(), be);

        LOG.info("Exception thrown [" + Arrays.toString(errors) + "]");

        return new LoaderException(LOADER_FAILURE_SEE_ERRORS + Arrays.toString(errors), errors);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param locale JAVADOC.
     * @param messageSource JAVADOC.
     * @param bindingResult JAVADOC.
     *
     * @return JAVADOC.
     */
    protected String[] getI18nErrors(final Locale locale, final BindingResult bindingResult) {
        List<String> errors = new ArrayList<String>();

        if ((bindingResult != null) && bindingResult.hasGlobalErrors()) {
            for (ObjectError objectError : bindingResult.getGlobalErrors()) {
                String message = objectError.getDefaultMessage();

                if (messageSource != null) {
                    Locale loc = ((locale != null) ? locale : Locale.getDefault());

                    try {
                        message = messageSource.getMessage(objectError.getCode(),
                                objectError.getArguments(), loc);
                    } catch (Throwable t) {
                        //do nothing (just stick with the default message)
                    }
                }

                errors.add(message);
            }
        }

        return errors.toArray(new String[errors.size()]);
    }
}
