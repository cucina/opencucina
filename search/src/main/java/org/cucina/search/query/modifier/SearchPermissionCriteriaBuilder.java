package org.cucina.search.query.modifier;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.FailingCriterion;
import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.PermissionDto;
import org.springframework.util.Assert;

import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchPermissionCriteriaBuilder
		implements PermissionCriteriaBuilder {
	private AccessFacade accessFacade;
	private PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper;

	/**
	 * Creates a new SearchPermissionCriteriaBuilder object.
	 */
	public SearchPermissionCriteriaBuilder(AccessFacade accessFacade,
										   PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper) {
		Assert.notNull(accessFacade, "accessFacade is null");
		this.accessFacade = accessFacade;
		Assert.notNull(permissionCriteriaBuilderHelper, "permissionCriteriaBuilderHelper is null");
		this.permissionCriteriaBuilderHelper = permissionCriteriaBuilderHelper;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param searchBean      JAVADOC.
	 * @param user            JAVADOC.
	 * @param applicationType JAVADOC.
	 * @param searchAlias     JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public SearchBean buildCriteria(SearchBean searchBean, String user, String applicationType,
									String searchAlias, String accessLevel) {
		Collection<PermissionDto> permissions = accessFacade.permissionsByUserTypeAccessLevel(user,
				applicationType, accessLevel);

		// if nothing, add a criterion which can never be true
		if (CollectionUtils.isEmpty(permissions)) {
			searchBean.addCriterion(new FailingCriterion(searchAlias));

			return searchBean;
		}

		SearchCriterion criterion = permissionCriteriaBuilderHelper.buildClause(applicationType,
				searchAlias, permissions);

		if (criterion != null) {
			searchBean.addCriterion(criterion);
		}

		return searchBean;
	}
}
