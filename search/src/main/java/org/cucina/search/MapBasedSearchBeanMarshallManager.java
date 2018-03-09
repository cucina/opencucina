package org.cucina.search;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.CompositeMap;
import org.apache.commons.collections.set.CompositeSet;
import org.cucina.core.InstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.search.marshall.*;
import org.cucina.search.query.SearchCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Map based implementation of SearchCriterionMarshallManager which makes use of
 * {@code SearchCriterionMarshaller} and {@code SearchCriterionUnmarshaller} for
 * marshalling and unmarshalling.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MapBasedSearchBeanMarshallManager
		implements SearchCriterionMarshallManager, InitializingBean, ApplicationContextAware {
	private static final Logger LOG = LoggerFactory.getLogger(MapBasedSearchBeanMarshallManager.class);
	private static final String DEFAULT = "default";
	private ApplicationContext applicationContext;
	private Map<String, SearchCriterionUnmarshaller> aliasKeyedUnmarshallers;
	private Map<Class<?>, SearchCriterionMarshaller> cachedMarshallers = new HashMap<Class<?>, SearchCriterionMarshaller>();
	private Map<String, SearchCriterionUnmarshaller> typeKeyedUnmarshallers;
	private Set<SearchCriterionMarshaller> marshallers;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param aliasKeyedUnmarshallers JAVADOC.
	 */
	@Required
	public void setAliasKeyedUnmarshallers(
			Map<String, SearchCriterionUnmarshaller> aliasKeyedUnmarshallers) {
		this.aliasKeyedUnmarshallers = aliasKeyedUnmarshallers;
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
		this.applicationContext = arg0;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param marshallers JAVADOC.
	 */
	@Required
	public void setMarshallers(Set<SearchCriterionMarshaller> marshallers) {
		this.marshallers = marshallers;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param typeKeyedUnmarshallers JAVADOC.
	 */
	@Required
	public void setTypeKeyedUnmarshallers(
			Map<String, SearchCriterionUnmarshaller> typeKeyedUnmarshallers) {
		this.typeKeyedUnmarshallers = typeKeyedUnmarshallers;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet()
			throws Exception {
		Assert.notNull(applicationContext, "applicationContext is null");
		Assert.isTrue(applicationContext.containsBean(SingletonBeanFactory.INSTANCE_FACTORY_ID),
				"ApplicationContext does not contain '" + SingletonBeanFactory.INSTANCE_FACTORY_ID +
						"' bean");

		// Unmarshallers
		Map<String, SearchCriterionUnmarshaller> defaultUnmarshallers = new HashMap<String, SearchCriterionUnmarshaller>();

		defaultUnmarshallers.put("Date", new DateSearchCriterionUnmarshaller());
		defaultUnmarshallers.put("Long", new LongSearchCriterionUnmarshaller());
		defaultUnmarshallers.put("Double", new DoubleSearchCriterionUnmarshaller());
		defaultUnmarshallers.put("long", new LongSearchCriterionUnmarshaller());
		defaultUnmarshallers.put("double", new DoubleSearchCriterionUnmarshaller());

		defaultUnmarshallers.put("default",
				new DefaultSearchCriterionUnmarshaller(
						(InstanceFactory) applicationContext.getBean(
								SingletonBeanFactory.INSTANCE_FACTORY_ID)));
		defaultUnmarshallers.put("boolean", new BooleanSearchCriterionUnmarshaller());

		if (typeKeyedUnmarshallers == null) {
			typeKeyedUnmarshallers = defaultUnmarshallers;
		} else {
			typeKeyedUnmarshallers = new CompositeMap(typeKeyedUnmarshallers, defaultUnmarshallers);
		}

		// Marshallers
		Set<SearchCriterionMarshaller> defaultMarshallers = new HashSet<SearchCriterionMarshaller>();

		defaultMarshallers.add(new DateSearchCriterionMarshaller());
		defaultMarshallers.add(new DateRelativeSearchCriterionMarshaller());
		defaultMarshallers.add(new ForeignKeySearchCriterionMarshaller());
		defaultMarshallers.add(new NumberSearchCriterionMarshaller());
		defaultMarshallers.add(new TextSearchCriterionMarshaller());
		defaultMarshallers.add(new InSearchCriterionMarshaller());
		defaultMarshallers.add(new BooleanSearchCriterionMarshaller());

		if (marshallers == null) {
			marshallers = defaultMarshallers;
		} else {
			marshallers = new CompositeSet(new Set[]{marshallers, defaultMarshallers});
		}
	}

	/**
	 * Marshalls according to the type of the criterion using Map marshallers.
	 *
	 * @param alias               String.
	 * @param propertyName        String.
	 * @param criterion           SearchCriterion.
	 * @param marshalledCriterion Map<String, Object>.
	 */
	@Override
	public void marshall(String alias, String propertyName, final SearchCriterion criterion,
						 Map<String, Object> marshalledCriterion) {
		Assert.isTrue((alias != null) || (propertyName != null),
				"must supply an alias and/or propertyName");
		Assert.notNull(criterion, "must supply criterion");
		Assert.notNull(marshalledCriterion, "must supply marshalledCriterion");

		SearchCriterionMarshaller marshaller = findMarshaller(criterion);

		if (marshaller != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Marshalling for propertyName [" + criterion.getClass().getSimpleName() +
						"]");
			}

			marshaller.marshall(alias, propertyName, criterion, marshalledCriterion);
		}
	}

	/**
	 * Unmarshalls according to the propertyType of the property using
	 * typeKeyedUnmarshallers.
	 *
	 * @param propertyType String.
	 * @param propertyName String.
	 * @param alias        String.
	 * @param rootType     String.
	 * @param rootAlias    String.
	 * @param criteria     Map<String, Object>.
	 * @return SearchCriterion.
	 */
	@Override
	public SearchCriterion unmarshall(String propertyType, String propertyName, String alias,
									  String rootType, String rootAlias, Map<String, Object> criteria) {
		Assert.notNull(propertyName, "must supply a propertyName");
		Assert.notNull(propertyType, "must supply a propertyType");
		Assert.notNull(alias, "must supply an alias");
		Assert.notNull(rootType, "must supply a rootType");
		Assert.notNull(rootAlias, "must supply a rootAlias");
		Assert.notNull(criteria, "must supply criteria");

		SearchCriterionUnmarshaller unmarshaller = typeKeyedUnmarshallers.get(propertyType);

		if (unmarshaller == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Checking for default unmarshaller for propertyType [" + propertyType +
						"]");
			}

			unmarshaller = typeKeyedUnmarshallers.get(DEFAULT);
		}

		if (unmarshaller != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Unmarshalling for propertyType [" + propertyType + "]");
			}

			return unmarshaller.unmarshall(propertyName, alias, rootType, rootAlias, criteria);
		}

		return null;
	}

	/**
	 * Should unmarshall according to the alias making use of
	 * aliasKeyedUnmarshallers.
	 *
	 * @param alias     String.
	 * @param rootType  String.
	 * @param rootAlias String.
	 * @param criteria  Map<String, Object>.
	 * @return SearchCriterion.
	 */
	@Override
	public SearchCriterion unmarshall(String alias, String rootType, String rootAlias,
									  Map<String, Object> criteria) {
		Assert.notNull(alias, "must supply an alias");
		Assert.notNull(rootType, "must supply a rootType");
		Assert.notNull(rootAlias, "must supply a rootAlias");
		Assert.notNull(criteria, "must supply criteria");

		SearchCriterionUnmarshaller unmarshaller = aliasKeyedUnmarshallers.get(alias);

		if (unmarshaller == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Checking for default unmarshaller for propertyName [" + alias + "]");
			}

			unmarshaller = aliasKeyedUnmarshallers.get(DEFAULT);
		}

		if (unmarshaller != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Unmarshalling for propertyName [" + alias + "]");
			}

			// assuming alias is same as property name, may well need to be
			// extended
			return unmarshaller.unmarshall(alias, alias, rootType, rootAlias, criteria);
		}

		return null;
	}

	private SearchCriterionMarshaller findMarshaller(final SearchCriterion criterion) {
		SearchCriterionMarshaller marshaller = cachedMarshallers.get(criterion.getClass());

		if (marshaller == null) {
			marshaller = (SearchCriterionMarshaller) CollectionUtils.find(marshallers,
					new Predicate() {
						@Override
						public boolean evaluate(Object arg0) {
							SearchCriterionMarshaller marshaller = (SearchCriterionMarshaller) arg0;

							return marshaller.supports(criterion.getClass());
						}
					});
			// caching here for later use
			cachedMarshallers.put(criterion.getClass(), marshaller);
		}

		return marshaller;
	}
}
