package org.cucina.engine.client.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.util.Assert;

import org.cucina.engine.client.Operation;
import org.cucina.engine.server.definition.WorkflowElementDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
  */
public class DtoOperationConverter
    implements Converter<WorkflowElementDto, Operation> {
    private static final Logger LOG = LoggerFactory.getLogger(DtoOperationConverter.class);
    private BeanResolver beanResolver;

    /**
     * Creates a new DtoOperationConverter object.
     *
     * @param beanResolver .
     */
    public DtoOperationConverter(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    /**
     *
     *
     * @param source .
     *
     * @return .
     */
    @Override
    public Operation convert(WorkflowElementDto source) {
        String path = source.getPath();

        Object obj;

        try {
            obj = beanResolver.resolve(null, path);
        } catch (AccessException e) {
            LOG.error("Oops", e);

            return null;
        }

        Assert.isInstanceOf(Operation.class, obj,
            "bean found at path:'" + path + "' is not a Operation");

        return (Operation) obj;
    }
}
