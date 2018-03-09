package org.cucina.security.access;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.security.bean.InstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractSecuredTypeRegistry
		implements SecuredTypeRegistry {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractSecuredTypeRegistry.class);
	private Collection<String> securableTypes;
	private InstanceFactory instanceFactory;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param instanceFactory JAVADOC.
	 */
	@Required
	public void setInstanceFactory(InstanceFactory instanceFactory) {
		this.instanceFactory = instanceFactory;
	}

	/**
	 * @param securableTypes
	 */
	public void setSecurableTypes(Collection<String> securableTypes) {
		this.securableTypes = securableTypes;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 * @see org.cucina.security.access.SecuredTypeRegistry#listSecurableProperties()
	 */
	@Override
	public Collection<String> listSecurableProperties() {
		Set<String> persistableProperties = new HashSet<String>();

		for (String type : listSecuredTypes()) {
			Class<?> beanClass = instanceFactory.getClassType(type);

			if (beanClass == null) {
				LOG.debug("Workflow type '" + type + "' is not a known class type");

				continue;
			}

			try {
				BeanInfo info = Introspector.getBeanInfo(beanClass);
				PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

				for (int i = 0; i < descriptors.length; i++) {
					//Check that there's a writeMethod properties like 'class' have readMethod but we're not interested in those here.
					if (descriptors[i].getWriteMethod() != null) {
						String propertyName = descriptors[i].getName();
						String propertyType = instanceFactory.getPropertyType(type, propertyName);

						if ((CollectionUtils.isEmpty(securableTypes) ||
								securableTypes.contains(propertyType)) &&
								instanceFactory.isForeignKey(type, propertyName)) {
							persistableProperties.add(propertyName + "." + propertyType);
						}
					}
				}
			} catch (IntrospectionException e) {
				LOG.error("Oops", e);
			}
		}

		return persistableProperties;
	}
}
