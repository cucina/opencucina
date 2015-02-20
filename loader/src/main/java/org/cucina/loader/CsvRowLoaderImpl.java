package org.cucina.loader;

import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.validation.Create;
import org.cucina.loader.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvRowLoaderImpl
    implements CsvRowLoader {
    private static final Logger LOG = LoggerFactory.getLogger(CsvRowLoaderImpl.class);
    private ConversionService conversionService;
    private Processor processor;
    private Validator validator;
    private boolean propagateErrors;

    /**
    * Creates a new CsvRowLoaderImpl object.
    *
    * @param validator JAVADOC.
    * @param conversionService JAVADOC.
    * @param propogateErrors JAVADOC.
    */
    public CsvRowLoaderImpl(Validator validator, ConversionService conversionService) {
        Assert.notNull(validator, "validator is null");
        this.validator = validator;
        Assert.notNull(conversionService, "conversionService is null");
        this.conversionService = conversionService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param processor
     *            JAVADOC.
     */
    @Required
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param propagateErrors JAVADOC.
     */
    public void setPropagateErrors(boolean propagateErrors) {
        this.propagateErrors = propagateErrors;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType
     *            JAVADOC.
     * @param headers
     *            JAVADOC.
     * @param line
     *            JAVADOC.
     * @param lineNumber
     *            JAVADOC.
     *
     * @throws BindException
     *             JAVADOC.
     */
    @Override
    public void processRow(String applicationType, String[] headers, String[] line,
        final int lineNumber)
        throws BindException {
        try {
            // this is not a failure case, simply a blank or incomplete line
            if ((line == null) || (line.length == 0) || (line.length < headers.length)) {
                LOG.warn("Line is not of required length [" + headers.length + "] for type [" +
                    applicationType + "]");

                return;
            }

            Object bean = populateBean(line, headers, applicationType, lineNumber);

            if (bean == null) {
                LOG.debug("Populator did not create a bean from the line");

                return;
            }

            validate(bean, applicationType, lineNumber);

            if (bean instanceof PersistableEntity) {
                LoadedObjectWrapper wrapper = new LoadedObjectWrapper((PersistableEntity) bean);

                processor.process(wrapper);
            } else {
                processor.process(bean);
            }
        } catch (BindException be) {
            if (propagateErrors) {
                throw be;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Error while processing row [" + lineNumber + "], message [" +
                    be.getMessage() + "]");
            }
        } catch (NestedRuntimeException nre) {
            LOG.warn("Oops", nre);

            if (propagateErrors) {
                BindException be = new BindException(line, "line");

                be.reject("loader.exception",
                    new Object[] { lineNumber, nre.getMostSpecificCause().getMessage() },
                    "There has been an exception");
                throw be;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Error while processing row [" + lineNumber + "], message [" +
                    nre.getMessage() + "]");
            }
        } catch (Exception e) {
            LOG.warn("Oops", e);

            if (propagateErrors) {
                BindException be = new BindException(line, "line");

                be.reject("loader.exception", new Object[] { lineNumber, e.getMessage() },
                    "There has been an exception");
                throw be;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Error while processing row [" + lineNumber + "], message [" +
                    e.getMessage() + "]");
            }
        }
    }

    private Object populateBean(String[] line, String[] headers, String applicationType,
        final int lineNumber)
        throws BindException {
        try {
            return conversionService.convert(new CsvlineWrapper(line, headers, applicationType),
                Object.class);
        } catch (BeansException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Oops", e);
            }

            BindException be = new BindException(Arrays.toString(headers), Arrays.toString(line));

            be.reject("loader.parseError",
                new Object[] { lineNumber, Arrays.toString(headers), Arrays.toString(line) },
                "Parse error [line: " + lineNumber + "]: " + Arrays.toString(headers) + ":" +
                Arrays.toString(line));

            throw be;
        }
    }

    private void validate(Object bean, String applicationType, int lineNumber)
        throws BindException {
        // default group
        Set<ConstraintViolation<Object>> violations = validator.validate(bean, Default.class,
                Create.class);

        if (!violations.isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed validation for " + ToStringBuilder.reflectionToString(bean));
            }

            // validation errors
            BindException be = new BindException(bean, applicationType);

            for (ConstraintViolation<Object> constraintViolation : violations) {
                be.reject("loader.validation.failure",
                    new Object[] {
                        lineNumber, constraintViolation.getPropertyPath(),
                        constraintViolation.getMessage()
                    }, constraintViolation.getMessage());

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Constraint:" +
                        ToStringBuilder.reflectionToString(constraintViolation));
                }
            }

            throw be;
        }

        return;
    }
}
