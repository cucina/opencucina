package org.cucina.engine.client.converters;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.util.Assert;

import org.cucina.engine.client.Check;
import org.cucina.engine.server.definition.ProcessElementDto;


/**
 *
 *
 * @author vlevine
  */
public class DtoCheckConverter
    implements Converter<ProcessElementDto, Check> {
    private static final Logger LOG = LoggerFactory.getLogger(DtoCheckConverter.class);
    private BeanResolver beanResolver;

    /**
     * Creates a new DtoCheckConverter object.
     *
     * @param beanResolver .
     */
    public DtoCheckConverter(BeanResolver beanResolver) {
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
    public Check convert(ProcessElementDto source) {
        String path = source.getPath();

        Object check;

        try {
            check = beanResolver.resolve(null, path);
        } catch (AccessException e) {
            LOG.error("Oops", e);

            return null;
        }

        Assert.isInstanceOf(Check.class, check, "bean found at path:'" + path + "' is not a Check");

        BeanWrapper bw = new BeanWrapperImpl(check);

        for (Map.Entry<String, Object> entry : source.getProperties().entrySet()) {
            if (bw.isWritableProperty(entry.getKey())) {
                bw.setPropertyValue(entry.getKey(), entry.getValue());
            }
        }

        return (Check) check;
    }
}
