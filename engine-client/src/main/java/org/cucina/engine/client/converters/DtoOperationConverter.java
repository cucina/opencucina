package org.cucina.engine.client.converters;

import org.cucina.engine.client.Operation;
import org.cucina.engine.server.definition.ProcessElementDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * @author vlevine
 */
public class DtoOperationConverter
		implements Converter<ProcessElementDto, Operation> {
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
	 * @param source .
	 * @return .
	 */
	@Override
	public Operation convert(ProcessElementDto source) {
		String path = source.getPath();

		Object op;

		try {
			op = beanResolver.resolve(null, path);
		} catch (AccessException e) {
			LOG.error("Oops", e);

			return null;
		}

		Assert.isInstanceOf(Operation.class, op,
				"bean found at path:'" + path + "' is not a Operation");

		BeanWrapper bw = new BeanWrapperImpl(op);

		for (Map.Entry<String, Object> entry : source.getProperties().entrySet()) {
			if (bw.isWritableProperty(entry.getKey())) {
				bw.setPropertyValue(entry.getKey(), entry.getValue());
			}
		}

		return (Operation) op;
	}
}
