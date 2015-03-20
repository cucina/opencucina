package org.cucina.engine.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import org.cucina.engine.definition.Station;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.service.ProcessSupportService;

import org.cucina.search.marshall.SearchCriterionMarshaller;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.AndSearchCriterion;
import org.cucina.search.query.criterion.FailingCriterion;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.OrSearchCriterion;
import org.cucina.search.query.modifier.PermissionCriteriaBuilder;
import org.cucina.search.query.modifier.PermissionCriteriaBuilderHelper;

import org.cucina.security.access.AccessRegistry;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Privilege;
import org.cucina.security.model.User;
import org.cucina.security.repository.PermissionRepository;
import org.cucina.security.repository.PrivilegeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowPermissionCriteriaBuilder
    implements PermissionCriteriaBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowPermissionCriteriaBuilder.class);
    private AccessRegistry accessRegistry;
    private PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper;
    private PermissionRepository permissionRepository;
    private PrivilegeRepository privilegeRepository;
    private ProcessSupportService workflowSupportService;

    /**
     * Creates a new WorkflowPermissionCriteriaBuilder object.
     *
     * @param accessRegistry JAVADOC.
     * @param permissionCriteriaBuilderHelper JAVADOC.
     * @param permissionDao JAVADOC.
     * @param rolePrivilegeDao JAVADOC.
     * @param workflowSupportService JAVADOC.
     */
    public WorkflowPermissionCriteriaBuilder(AccessRegistry accessRegistry,
        PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper,
        PermissionRepository permissionRepository, PrivilegeRepository privilegeRepository,
        ProcessSupportService workflowSupportService) {
        Assert.notNull(accessRegistry, "accessRegistry is null");
        this.accessRegistry = accessRegistry;
        Assert.notNull(permissionCriteriaBuilderHelper, "permissionCriteriaBuilderHelper is null");
        this.permissionCriteriaBuilderHelper = permissionCriteriaBuilderHelper;
        Assert.notNull(permissionRepository, "permissionRepository is null");
        this.permissionRepository = permissionRepository;
        Assert.notNull(privilegeRepository, "privilegeRepository is null");
        this.privilegeRepository = privilegeRepository;
        Assert.notNull(workflowSupportService, "workflowSupportService is null");
        this.workflowSupportService = workflowSupportService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param searchBean JAVADOC.
     * @param user JAVADOC.
     * @param applicationType JAVADOC.
     * @param searchAlias JAVADOC.
     * @param accessLevel JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public SearchBean buildCriteria(SearchBean searchBean, User user, String applicationType,
        String searchAlias, String accessLevel) {
        Map<Privilege, Collection<String>> stateByPriv = extractStateByPriv(applicationType);
        boolean hasPermissions = false;

        List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();

        for (Map.Entry<Privilege, Collection<String>> entry : stateByPriv.entrySet()) {
            //if there are no states, we don't need the criteria
            if (CollectionUtils.isEmpty(entry.getValue())) {
                continue;
            }

            Collection<Permission> permissions = permissionRepository.findByUserAndPrivilege(user,
                    entry.getKey());

            if (!CollectionUtils.isEmpty(permissions)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("For privilege " + entry.getKey() + " " + user + " had permissions " +
                        permissions);
                }

                hasPermissions = true;

                SearchCriterion criterion = buildTaskListClause(applicationType, searchAlias,
                        permissions, entry.getValue());

                if (criterion != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Built criterion " + criterion);
                    }

                    criteria.add(criterion);
                }
            }
        }

        if (criteria.size() > 0) {
            if (criteria.size() == 1) {
                searchBean.addCriterion(criteria.iterator().next());
            } else {
                //more than one, need to OR them together
                searchBean.addCriterion(new OrSearchCriterion(searchAlias, criteria));
            }
        }

        //no relevant permissions should result in nothing being returned in task list
        if (!hasPermissions) {
            searchBean.addCriterion(new FailingCriterion(searchAlias));
        }

        return searchBean;
    }

    private SearchCriterion buildTaskListClause(String typeName, String searchAlias,
        Collection<Permission> permissions, Collection<String> states) {
        if (CollectionUtils.isEmpty(permissions)) {
            return null;
        }

        SearchCriterion criterion = permissionCriteriaBuilderHelper.buildClause(typeName,
                searchAlias, permissions);

        List<SearchCriterion> jointCriteria = new ArrayList<SearchCriterion>();

        if (criterion != null) {
            jointCriteria.add(criterion);
        }

        if (!CollectionUtils.isEmpty(states)) {
            SearchCriterion statesCrit = new InSearchCriterion("history.token.placeId", null,
                    SearchCriterionMarshaller.HISTORY_ALIAS, states);
            Map<String, String> parentInAliases = new HashMap<String, String>();

            parentInAliases.put("token", "token");
            statesCrit.setParentAliases(parentInAliases);

            jointCriteria.add(statesCrit);
        }

        if (jointCriteria.size() == 0) {
            return null;
        }

        if (jointCriteria.size() == 1) {
            return jointCriteria.iterator().next();
        }

        //combination of permissions criteria and task list states should be anded together
        return new AndSearchCriterion(searchAlias, jointCriteria);
    }

    private Map<Privilege, Collection<String>> extractStateByPriv(String applicationType) {
        //TODO lookup from type -> workflow id
        Collection<Transition> actionableTransitions = workflowSupportService.listActionableTransitions(applicationType);
        Map<Privilege, Collection<String>> stateByPriv = new HashMap<Privilege, Collection<String>>();

        //foreach transition then we see if user has that privilege
        //if they do, get begin place and add to a map for that privilege
        for (Transition transition : actionableTransitions) {
            Collection<Privilege> privs = new HashSet<Privilege>();
            Collection<String> privNames = transition.getPrivilegeNames();

            if (CollectionUtils.isEmpty(privNames)) {
                privs.add(accessRegistry.getDefaultPrivilege());
            } else {
                for (String privName : privNames) {
                    Privilege privilege = privilegeRepository.findByName(privName);

                    if (privilege != null) {
                        privs.add(privilege);
                    }
                }
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("For transition " + transition + " found privileges " + privs);
            }

            for (Privilege privilege : privs) {
                if (!stateByPriv.containsKey(privilege)) {
                    stateByPriv.put(privilege, new HashSet<String>());
                }

                if (transition.getInput() instanceof Station) {
                    stateByPriv.get(privilege).add(transition.getInput().getId());
                }
            }
        }

        return stateByPriv;
    }
}
