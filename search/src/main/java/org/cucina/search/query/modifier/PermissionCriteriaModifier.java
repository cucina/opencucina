package org.cucina.search.query.modifier;

import org.apache.commons.collections.MapUtils;
import org.cucina.search.SearchType;
import org.cucina.search.query.SearchBean;
import org.cucina.security.api.CurrentUserAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PermissionCriteriaModifier
		extends AbstractCriteriaModifier {
	/**
	 * search
	 */
	public static final String DEFAULT_ACCESS_LEVEL = "search";
	private static final Logger LOG = LoggerFactory.getLogger(PermissionCriteriaModifier.class);
	private Map<String, PermissionCriteriaBuilder> criteriaBuilders = Collections.emptyMap();
	private String accessLevel = DEFAULT_ACCESS_LEVEL;
	private boolean permissionsInUse = true;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param criteriaBuilders JAVADOC.
	 */
	public PermissionCriteriaModifier(Map<String, PermissionCriteriaBuilder> criteriaBuilders) {
		this.criteriaBuilders = criteriaBuilders;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param accessLevel JAVADOC.
	 */
	public void setAccessLevel(String accessLevel) {
		Assert.notNull(accessLevel);
		this.accessLevel = accessLevel;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param permissionsInUse JAVADOC.
	 */
	public void setPermissionsInUse(boolean permissionsInUse) {
		this.permissionsInUse = permissionsInUse;
	}

	/**
	 * Modifies searchBean to include restrictions supplied
	 *
	 * @param searchBean JAVADOC.
	 * @param params     JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected SearchBean doModify(SearchBean searchBean, Map<String, Object> params) {
		// TODO supply searchType, accessLevel in params to override the one in
		// searchBean
		Assert.notNull(searchBean, "searchBean cannot be null");

		if (!permissionsInUse) {
			LOG.warn(
					"Permissions criteria modifier switched off, currently. Permissions not in use when searching");

			return searchBean;
		}

		Map<String, String> aliasByType = searchBean.getAliasByType();

		Assert.isTrue(MapUtils.isNotEmpty(aliasByType),
				"Should be at least one search type in the searchBean");

		String user = CurrentUserAccessor.getCurrentUserName();

		String applicationType = aliasByType.keySet().iterator().next();
		String searchAlias = aliasByType.get(applicationType);

		PermissionCriteriaBuilder pcb = criteriaBuilders.get(searchBean.getSearchType().toString());

		if (pcb == null) {
			pcb = criteriaBuilders.get(SearchType.DEFAULT.toString());
		}

		return pcb.buildCriteria(searchBean, user, applicationType, searchAlias, accessLevel);
	}
}
