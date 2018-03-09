package org.cucina.engine.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.engine.DefaultProcessEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EnvironmentBeanDefinitionParser
		extends AbstractDefaultIdBeanDefinitionParser {
	private static final Logger LOG = LoggerFactory.getLogger(EnvironmentBeanDefinitionParser.class);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return DefaultProcessEnvironment.class;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	protected String getDefaultId() {
		return "workflowEnvironment";
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @param builder JAVADOC.
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		builder.addPropertyReference("tokenFactory", element.getAttribute("tokenFactory"));

		List<Element> resources = DomUtils.getChildElementsByTagName(element, "definition");

		Collection<String> resnames = new ArrayList<String>();

		for (Element relement : resources) {
			resnames.add(relement.getNodeValue());
		}

		builder.addPropertyValue("definitionResources", buildResources(resnames));

		List<Element> listeners = DomUtils.getChildElementsByTagName(element, "listenerRef");

		List<BeanReference> lisnames = new ManagedList<BeanReference>();

		for (Element lelement : listeners) {
			lisnames.add(new RuntimeBeanReference(lelement.getNodeValue()));
		}

		builder.addPropertyValue("workflowListeners", lisnames);
	}

	private Collection<Resource> buildResources(Collection<String> resnames) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Collection<Resource> colres = new HashSet<Resource>();

		for (String string : resnames) {
			Resource[] resources;

			try {
				resources = resolver.getResources(string.trim());
				colres.addAll(Arrays.asList(resources));
			} catch (IOException e) {
				LOG.error("Oops", e);
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Parsed resources:" + colres);
		}

		return colres;
	}
}
