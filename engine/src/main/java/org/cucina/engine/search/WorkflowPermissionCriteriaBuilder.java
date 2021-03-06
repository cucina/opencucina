package org.cucina.engine.search;

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
import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.PermissionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowPermissionCriteriaBuilder
		implements PermissionCriteriaBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowPermissionCriteriaBuilder.class);
	private AccessFacade accessFacade;
	private PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper;
	private ProcessSupportService workflowSupportService;

	/**
	 * Creates a new WorkflowPermissionCriteriaBuilder object.
	 *
	 * @param accessFacade                    JAVADOC.
	 * @param permissionCriteriaBuilderHelper JAVADOC.
	 * @param permissionDao                   JAVADOC.
	 * @param rolePrivilegeDao                JAVADOC.
	 * @param workflowSupportService          JAVADOC.
	 */
	public WorkflowPermissionCriteriaBuilder(AccessFacade accessFacade,
											 PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper,
											 ProcessSupportService workflowSupportService) {
		Assert.notNull(accessFacade, "accessFacade is null");
		this.accessFacade = accessFacade;
		Assert.notNull(permissionCriteriaBuilderHelper, "permissionCriteriaBuilderHelper is null");
		this.permissionCriteriaBuilderHelper = permissionCriteriaBuilderHelper;
		Assert.notNull(workflowSupportService, "workflowSupportService is null");
		this.workflowSupportService = workflowSupportService;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param searchBean      JAVADOC.
	 * @param user            JAVADOC.
	 * @param applicationType JAVADOC.
	 * @param searchAlias     JAVADOC.
	 * @param accessLevel     JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public SearchBean buildCriteria(SearchBean searchBean, String user, String applicationType,
									String searchAlias, String accessLevel) {
		Map<String, Collection<String>> stateByPriv = extractStateByPriv(applicationType);
		boolean hasPermissions = false;

		List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();

		for (Map.Entry<String, Collection<String>> entry : stateByPriv.entrySet()) {
			//if there are no states, we don't need the criteria
			if (CollectionUtils.isEmpty(entry.getValue())) {
				continue;
			}

			Collection<PermissionDto> permissions = accessFacade.permissionsByUserAndPrivilege(user,
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
												Collection<PermissionDto> permissions, Collection<String> states) {
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

	private Map<String, Collection<String>> extractStateByPriv(String applicationType) {
		//TODO lookup from type -> workflow id
		Collection<Transition> actionableTransitions = workflowSupportService.listActionableTransitions(applicationType);
		Map<String, Collection<String>> stateByPriv = new HashMap<String, Collection<String>>();

		//foreach transition then we see if user has that privilege
		//if they do, get begin place and add to a map for that privilege
		for (Transition transition : actionableTransitions) {
			Collection<String> privs = transition.getPrivilegeNames();

			if (CollectionUtils.isEmpty(privs)) {
				privs = Collections.singleton(accessFacade.getDefaultPrivilege());
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("For transition " + transition + " found privileges " + privs);
			}

			for (String privilege : privs) {
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
