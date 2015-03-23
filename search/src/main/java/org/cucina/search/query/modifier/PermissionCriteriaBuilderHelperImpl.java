package org.cucina.search.query.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.AndSearchCriterion;
import org.cucina.search.query.criterion.ForeignKeySearchCriterion;
import org.cucina.search.query.criterion.OrSearchCriterion;
import org.cucina.security.api.DimensionDto;
import org.cucina.security.api.PermissionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PermissionCriteriaBuilderHelperImpl
    implements PermissionCriteriaBuilderHelper {
    private static final Logger LOG = LoggerFactory.getLogger(PermissionCriteriaBuilderHelperImpl.class);
    private InstanceFactory instanceFactory;

    /**
     * Creates a new AbstractPermissionCriteriaBuilder object.
     *
     * @param permissionsHelper
     *            JAVADOC.
     */
    public PermissionCriteriaBuilderHelperImpl(InstanceFactory instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param typeName
     *            JAVADOC.
     * @param searchAlias
     *            JAVADOC.
     * @param permissions
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see org.cucina.search.query.modifier.PermissionCriteriaBuilderHelper#buildClause(java.lang.String,
     *      java.lang.String, java.util.Collection)
     */
    @Override
    public SearchCriterion buildClause(String typeName, String searchAlias,
        Collection<PermissionDto> permissions) {
        List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();

        Map<String, String> parentAliases = new HashMap<String, String>();

        parentAliases.put(searchAlias, searchAlias);

        for (PermissionDto permission : permissions) {
            List<SearchCriterion> critPerPermission = new ArrayList<SearchCriterion>();
            Map<String, Collection<Long>> clause = relevantDimensions(typeName, permission);

            if (clause.keySet().size() == 0) {
                LOG.info("Failed to find any clauses for '" + typeName + "' and " + permission);

                return null;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Found clause " + clause);
            }

            for (Map.Entry<String, Collection<Long>> entry : clause.entrySet()) {
                SearchCriterion criterion = new ForeignKeySearchCriterion(entry.getKey(), null,
                        searchAlias, entry.getValue());

                criterion.setParentAliases(parentAliases);

                critPerPermission.add(criterion);
            }

            if (critPerPermission.size() == 0) {
                // shouldn't happen
                continue;
            }

            criteria.add((critPerPermission.size() == 1) ? critPerPermission.iterator().next()
                                                         : new AndSearchCriterion(searchAlias,
                    critPerPermission));
        }

        // shouldn't happen, as this should be caught by clause above which
        // returns null
        if (criteria.size() == 0) {
            return null;
        }

        if (criteria.size() == 1) {
            return criteria.iterator().next();
        }

        return new OrSearchCriterion(searchAlias, criteria);
    }

    private Map<String, Collection<Long>> relevantDimensions(String typeName,
        PermissionDto permission) {
        Map<String, Collection<Long>> byProperty = new HashMap<String, Collection<Long>>();

        for (DimensionDto dimension : permission.getDimensions()) {
            String propertyName = dimension.getPropertyName();

            if (StringUtils.isNotEmpty(instanceFactory.getPropertyType(typeName, propertyName))) {
                Collection<Long> objects = byProperty.get(propertyName);

                if (objects == null) {
                    objects = new HashSet<Long>();
                    byProperty.put(propertyName, objects);
                }

                objects.add(dimension.getDomainObjectId());
            }
        }

        return byProperty;
    }
}
