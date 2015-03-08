package org.cucina.search.query.modifier;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.FailingCriterion;
import org.cucina.security.access.AccessRegistry;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Privilege;
import org.cucina.security.model.User;
import org.cucina.security.repository.PermissionRepository;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchPermissionCriteriaBuilder
    implements PermissionCriteriaBuilder {
    private AccessRegistry accessRegistry;
    private PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper;
    private PermissionRepository permissionRepository;

    /**
     * Creates a new SearchPermissionCriteriaBuilder object.
     */
    public SearchPermissionCriteriaBuilder(AccessRegistry accessRegistry,
        PermissionRepository permissionDao,
        PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper) {
        Assert.notNull(accessRegistry, "accessRegistry is null");
        this.accessRegistry = accessRegistry;
        Assert.notNull(permissionDao, "permissionDao is null");
        this.permissionRepository = permissionDao;
        Assert.notNull(permissionCriteriaBuilderHelper, "permissionCriteriaBuilderHelper is null");
        this.permissionCriteriaBuilderHelper = permissionCriteriaBuilderHelper;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param searchBean
     *            JAVADOC.
     * @param user
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     * @param searchAlias
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public SearchBean buildCriteria(SearchBean searchBean, User user, String applicationType,
        String searchAlias, String accessLevel) {
        Privilege privilege = accessRegistry.lookup(applicationType, accessLevel);

        Assert.notNull(privilege, "privilege must be configured in accessRegistry");

        Collection<Permission> permissions = permissionRepository.findByUserAndPrivilege(user,
                privilege);

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
