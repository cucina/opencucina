package org.cucina.search;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.search.query.SearchBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SimpleProjectionPopulator
		implements ProjectionPopulator {
	private static final Logger LOG = LoggerFactory.getLogger(ExpressionProjectionPopulator.class);
	private SearchBeanFactory searchBeanFactory;

	/**
	 * Creates a new SimpleProjectionPopulator object which
	 * delegates to the <code>SearchBeanFactory</code> to add
	 * projections
	 *
	 * @param instanceFactory   JAVADOC.
	 * @param projectionFactory JAVADOC.
	 */
	public SimpleProjectionPopulator(SearchBeanFactory searchBeanFactory) {
		Assert.notNull(searchBeanFactory, "searchBeanFactory cannot be null");
		this.searchBeanFactory = searchBeanFactory;
	}

	/**
	 * Looks for "projections" list in params map. If no
	 * list is found it delegates to the SearchBeanFactory for default configuration.
	 *
	 * @param type JAVADOC.
	 * @param bean JAVADOC.
	 */
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public void populate(String type, SearchBean bean, Map<String, Object> params,
						 Class<?>... projectionGroup) {
		Assert.notNull(bean, "bean cannot be null");
		Assert.notNull(params, "params cannot be null");

		Object pl = params.get(ProjectionPopulator.PROJECTIONS);
		Collection<String> projections = null;

		if (pl instanceof Collection) {
			projections = (Collection<String>) pl;
		} else {
			String projectionList = null;

			if (pl instanceof String[] && (((String[]) pl).length > 0)) {
				projectionList = ((String[]) pl)[0];
			} else if (pl instanceof String) {
				projectionList = (String) pl;
			}

			if (StringUtils.isNotEmpty(projectionList)) {
				projections = Arrays.asList(projectionList.split(","));
			}
		}

		if (CollectionUtils.isNotEmpty(projections)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Deserialising projections for type='" + type +
						"' from projectionList [" + projections + "]");
			}

			bean.getProjections().clear();

			processProjections(type, bean, projections);
		} else {
			// If no projections list add default list
			if (LOG.isDebugEnabled()) {
				LOG.debug("No projections specified for type='" + type + " so using default list");
			}

			searchBeanFactory.addProjections(type, bean, projectionGroup);
		}
	}

	/**
	 * Adds the projections to the <code>SearchBean</code>
	 *
	 * @param bean
	 * @param projections
	 */
	protected void processProjections(String type, SearchBean bean, Collection<String> projections,
									  Class<?>... projectionGroup) {
		searchBeanFactory.addProjections(type, bean, projections, projectionGroup);
	}
}
