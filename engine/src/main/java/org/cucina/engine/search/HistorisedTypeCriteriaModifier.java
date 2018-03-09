package org.cucina.engine.search;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.marshall.SearchCriterionMarshaller;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.JoinCriterion;
import org.cucina.search.query.criterion.SubSelectCriterion;
import org.cucina.search.query.modifier.AbstractCriteriaModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * CriteriaModifier for historised types which adds history specific criterion.
 *
 * @author hkelsey
 */
public class HistorisedTypeCriteriaModifier
		extends AbstractCriteriaModifier {
	private static final Logger LOG = LoggerFactory.getLogger(HistorisedTypeCriteriaModifier.class);
	private Collection<String> historisedTypes;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param historisedTypes JAVADOC.
	 */
	public void setHistorisedTypes(Collection<String> historisedTypes) {
		this.historisedTypes = historisedTypes;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param searchBean JAVADOC.
	 * @param params     JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected SearchBean doModify(SearchBean searchBean, Map<String, Object> params) {
		String subjectType = (String) params.get(PersistableEntity.APPLICATION_TYPE);

		Assert.notNull(subjectType, "type of search must be provided to criteria modifiers");

		if ((historisedTypes != null) && historisedTypes.contains(subjectType)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding history criterion to search");
			}

			String subjectAlias = searchBean.getAliasByType().get(subjectType);

			Assert.notNull(subjectAlias,
					"subjectAlias must be set up in SearchBean for type [" + subjectType + "]");

			searchBean.addCriterion(new JoinCriterion(SearchCriterionMarshaller.HISTORY_ALIAS,
					"id", subjectAlias, "token.domainObjectId",
					SearchCriterionMarshaller.HISTORY_ALIAS));
			/* Use an InSearchCriterion here because it will result in an eq restriction for better performance rather than the 'like'
			 * TextSearchCriterion produces as it's not required here.
			 */
			searchBean.addCriterion(new InSearchCriterion("token.domainObjectType", null,
					SearchCriterionMarshaller.HISTORY_ALIAS, Collections.singleton(subjectType)));
			searchBean.addCriterion(new SubSelectCriterion("id",
					SearchCriterionMarshaller.HISTORY_ALIAS,
					"select max(hr.id) from HistoryRecord hr " +
							"where hr.token.domainObjectId = " + NameUtils.concat(subjectAlias, "id") +
							" and hr.token.domainObjectType = '" + subjectType + "'"));
		}

		return searchBean;
	}
}
