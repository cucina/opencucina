package org.cucina.search;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.ProjectionFactory;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.projection.Projection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;


/**
 * Default Projection Populator looks for "projections" list in params map, this
 * will be csv with the projections in the format Type.parameterName, e.g.
 * Excess.excessCleared or Excess.dataset.id for nested projections.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ExpressionProjectionPopulator
		extends SimpleProjectionPopulator {
	private static final Logger LOG = LoggerFactory.getLogger(ExpressionProjectionPopulator.class);
	private InstanceFactory instanceFactory;
	private ProjectionFactory projectionFactory;

	/**
	 * Creates a new ExpressionProjectionPopulator object which expects the
	 * projections in the form of an object property expression.
	 *
	 * @param instanceFactory   JAVADOC.
	 * @param projectionFactory JAVADOC.
	 */
	public ExpressionProjectionPopulator(InstanceFactory instanceFactory,
										 ProjectionFactory projectionFactory, SearchBeanFactory searchBeanFactory) {
		super(searchBeanFactory);
		Assert.notNull(instanceFactory, "instanceFactory cannot be null");
		this.instanceFactory = instanceFactory;
		Assert.notNull(projectionFactory, "projectionFactory cannot be null");
		this.projectionFactory = projectionFactory;
	}

	/**
	 * Projections in the format parameterName, e.g. excessCleared or dataset.id
	 * for nested projections. If no list is found it delegates to the
	 * SearchBeanFactory for default configuration.
	 *
	 * @param type JAVADOC.
	 * @param bean JAVADOC.
	 */
	@Override
	protected void processProjections(String type, SearchBean bean, Collection<String> projections,
									  Class<?>... projectionGroup) {
		Assert.notNull(bean, "bean cannot be null");
		Assert.notNull(projections, "projections cannot be null");
		bean.getProjections().clear();

		for (String namedProjection : projections) {
			namedProjection = namedProjection.trim();

			String aggregateFunction = null;

			if (namedProjection.contains("(")) {
				Assert.isTrue(namedProjection.indexOf("(") > 2,
						"The aggregate function is not formed correctly [" + namedProjection + "]");
				Assert.isTrue(namedProjection.indexOf(")") > namedProjection.indexOf("("),
						"The aggregate function is not formed correctly [" + namedProjection + "]");
				// Get aggregate function, and then trim up string to leave
				// projection
				aggregateFunction = namedProjection.substring(0, namedProjection.indexOf("("));
				namedProjection = namedProjection.substring(namedProjection.indexOf("(") + 1,
						namedProjection.indexOf(")"));
			}

			String rootType = NameUtils.getType(namedProjection);

			Assert.notNull(rootType, "Should have supplied type in projection");

			String rootAlias = bean.getAliasByType().get(rootType);

			Assert.notNull(rootAlias, "No rootAlias for type [" + rootType + "]");

			String propertyName = namedProjection.substring(rootType.length() + 1);

			// Just check it exists
			String propertyType = instanceFactory.getPropertyType(rootType, propertyName);

			if (StringUtils.isNotEmpty(propertyType)) {
				Projection projection = projectionFactory.create(rootType, propertyName, null,
						rootAlias, aggregateFunction);

				if (LOG.isDebugEnabled()) {
					LOG.debug("Adding projection [" + projection + "]");
				}

				bean.addProjection(projection);
			} else {
				LOG.error("Invalid projection for search [" + namedProjection + "]");
			}
		}
	}
}
