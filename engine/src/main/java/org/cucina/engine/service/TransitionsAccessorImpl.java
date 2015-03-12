package org.cucina.engine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.cucina.core.model.PersistableEntity;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;

import org.cucina.security.access.AccessManager;
import org.cucina.security.access.AccessRegistry;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TransitionsAccessorImpl
    implements TransitionsAccessor {
    private AccessManager<UserDetails> accessManager;
    private AccessRegistry accessRegistry;
    private ProcessEnvironment workflowEnvironment;

    /**
     * Creates a new TransitionAccessorImpl object.
     */
    public TransitionsAccessorImpl(AccessRegistry accessRegistry,
        AccessManager<UserDetails> accessManager, ProcessEnvironment workflowEnvironment) {
        Assert.notNull(accessRegistry, "accessRegistry should not be null");
        this.accessRegistry = accessRegistry;
        Assert.notNull(accessManager, "accessManager should not be null");
        this.accessManager = accessManager;
        Assert.notNull(workflowEnvironment, "workflowEnvironment should not be null");
        this.workflowEnvironment = workflowEnvironment;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowDefinitionId JAVADOC.
     * @param placeId JAVADOC.
     * @param entity JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<String> listPermittedTransitions(String workflowDefinitionId, String placeId,
        PersistableEntity entity) {
        Assert.notNull(entity);

        Map<String, Object> map;

        try {
            map = new PropertyUtilsBean().describe(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failure to convert object to map", e);
        }

        return listPermittedTransitions(workflowDefinitionId, placeId, entity.getApplicationType(),
            map);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowDefinitionId JAVADOC.
     * @param placeId JAVADOC.
     * @param applicationType JAVADOC.
     * @param map JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings({"unchecked"})
    @Override
    @Transactional
    public Collection<String> listPermittedTransitions(String workflowDefinitionId, String placeId,
        String applicationType, Map<String, Object> map) {
        Assert.notNull(workflowDefinitionId, "workflowDefinitionId cannot be null");
        Assert.notNull(placeId, "placeId cannot be null");

        ProcessDefinition wflDefinition = workflowEnvironment.getDefinitionRegistry()
                                                             .findWorkflowDefinition(workflowDefinitionId);
        State place = wflDefinition.findPlace(placeId);

        Assert.notNull(place,
            "place cannot be null for wflwId [" + workflowDefinitionId + "] and place id [" +
            placeId + "]");

        Collection<Transition> permittedTransitions = new ArrayList<Transition>();

        Map<String, Boolean> privilegeMap = new HashMap<String, Boolean>();
        String defaultPrivilegeName = accessRegistry.getDefaultPrivilege().getName();

        for (Transition transition : place.getAllTransitions()) {
            Collection<String> privilegeNames = transition.getPrivilegeNames();

            if (CollectionUtils.isEmpty(privilegeNames)) {
                privilegeNames = Collections.singleton(defaultPrivilegeName);
            }

            for (String privilege : privilegeNames) {
                Boolean cached = privilegeMap.get(privilege);

                if (((cached != null) && cached.booleanValue()) ||
                        accessManager.hasPermission(privilege, applicationType, map)) {
                    privilegeMap.put(privilege, true);
                    permittedTransitions.add(transition);

                    break;
                }

                privilegeMap.put(privilege, false);
            }
        }

        return CollectionUtils.collect(permittedTransitions,
            new Transformer() {
                @Override
                public Object transform(Object arg0) {
                    if (arg0 == null) {
                        return null;
                    }

                    return ((Transition) arg0).getId();
                }
            });
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowDefinitionId
     *            JAVADOC.
     * @param placeId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> listPermittedTransitionsNoObjectCheck(String workflowDefinitionId,
        String placeId) {
        Assert.notNull(workflowDefinitionId, "workflowDefinitionId cannot be null");
        Assert.notNull(placeId, "placeId cannot be null");

        ProcessDefinition wflDefinition = workflowEnvironment.getDefinitionRegistry()
                                                             .findWorkflowDefinition(workflowDefinitionId);
        State place = wflDefinition.findPlace(placeId);

        Assert.notNull(place,
            "place cannot be null for wflwId [" + workflowDefinitionId + "] and place id [" +
            placeId + "]");

        Collection<Transition> permittedTransitions = new ArrayList<Transition>();

        for (Transition transition : place.getAllTransitions()) {
            if (CollectionUtils.isNotEmpty(transition.getPrivilegeNames())) {
                for (String privilege : transition.getPrivilegeNames()) {
                    if (accessManager.hasPrivilege(privilege)) {
                        permittedTransitions.add(transition);

                        break;
                    }
                }
            } else {
                permittedTransitions.add(transition);
            }
        }

        return CollectionUtils.collect(permittedTransitions,
            new Transformer() {
                @Override
                public Object transform(Object arg0) {
                    if (arg0 == null) {
                        return null;
                    }

                    return ((Transition) arg0).getId();
                }
            });
    }
}
