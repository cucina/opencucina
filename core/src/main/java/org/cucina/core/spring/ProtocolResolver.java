package org.cucina.core.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

import java.util.HashMap;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProtocolResolver
		implements BeanResolver, ApplicationContextAware, InitializingBean {
	/**
	 * The default protocol to use to resolve objects. The default is
	 * <code>bean</code> to look for a Spring bean with the id specified in
	 * "path" attribute.
	 */
	public static final String DEFAULT_PROTOCOL = "bean";
	/**
	 * :
	 */
	public static final char PROTOCOL_SEPARATOR = ':';
	private static final Logger LOG = LoggerFactory.getLogger(ProtocolResolver.class);
	private ApplicationContext beanFactory;
	private Map<String, BeanResolver> resolvers;

	/**
	 * Creates a new ProtocolResolver object.
	 */
	public ProtocolResolver() {
		this.resolvers = new HashMap<String, BeanResolver>();
	}

	/**
	 * Creates a new ResolverObjectFactoryImpl object.
	 *
	 * @param resolverRegistry JAVADOC.
	 */
	public ProtocolResolver(Map<String, BeanResolver> resolvers) {
		this();

		if (resolvers != null) {
			this.resolvers.putAll(resolvers);
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 * @throws BeansException JAVADOC.
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.beanFactory = arg0;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Override
	public void afterPropertiesSet()
			throws Exception {
		if (!resolvers.containsKey("bean")) {
			resolvers.put("bean", new BeanFactoryResolver(beanFactory));
		}

		if (!resolvers.containsKey("class")) {
			resolvers.put("class", new ClassResolver());
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param context  JAVADOC.
	 * @param beanName JAVADOC.
	 * @return JAVADOC.
	 * @throws AccessException JAVADOC.
	 */
	@Override
	public Object resolve(EvaluationContext context, String beanName)
			throws AccessException {
		int index = beanName.indexOf(PROTOCOL_SEPARATOR);

		String protocol = DEFAULT_PROTOCOL;

		if (index >= 0) {
			protocol = beanName.substring(0, index);
		}

		BeanResolver resolver = resolvers.get(protocol);

		if (resolver == null) {
			LOG.error("There is no resolver registered for protocol '" + protocol + "]");

			throw new IllegalArgumentException("There is no resolver registered for protocol '" +
					protocol + "]");
		}

		String componentPath = beanName;

		if (index >= 0) {
			componentPath = beanName.substring(index + 1);
		}

		Object result = resolver.resolve(context, componentPath);

		if (result == null) {
			LOG.error("Failed to resolve object referred by protocol '" + protocol +
					"' and path '" + componentPath + "'.");

			throw new IllegalArgumentException("Failed to resolve object referred by protocol '" +
					protocol + "' and path '" + componentPath + "'.");
		}

		return result;
	}
}
