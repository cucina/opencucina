package org.cucina.engine.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.OptimisticLockException;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.cucina.core.model.Attachment;
import org.cucina.core.model.PersistableEntity;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Station;
import org.cucina.engine.definition.Token;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.listeners.HistoryListener;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.HistoryRecordRepository;
import org.cucina.engine.repository.TokenRepository;

import org.cucina.i18n.api.ListNodeDto;

import org.cucina.search.SearchBeanFactory;
import org.cucina.search.SearchService;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchResults;

import org.cucina.security.access.AccessRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Service
public class ProcessSupportServiceImpl
    implements ProcessSupportService {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessSupportServiceImpl.class);
    private AccessRegistry accessRegistry;
    private DefinitionService definitionService;
    private HistoryRecordRepository historyRecordRepository;
    private ProcessEnvironment processEnvironment;
    private SearchBeanFactory searchBeanFactory;
    private SearchService searchService;
    private TokenRepository tokenRepository;

    /**
    * JAVADOC Method Level Comments
    *
    * @param accessRegistry
    *            JAVADOC.
    */
    @Required
    @Autowired
    public void setAccessRegistry(AccessRegistry accessRegistry) {
        this.accessRegistry = accessRegistry;
    }

    /**
     * Set definitionService
     *
     * @param definitionService
     *            DefinitionService
     */
    @Required
    @Autowired
    public void setDefinitionService(DefinitionService definitionService) {
        this.definitionService = definitionService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param historyRecordRepository JAVADOC.
     */
    @Required
    @Autowired
    public void setHistoryRecordRepository(HistoryRecordRepository historyRecordRepository) {
        this.historyRecordRepository = historyRecordRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowEnvironment
     *            JAVADOC.
     */
    @Required
    @Autowired
    public void setProcessEnvironment(ProcessEnvironment processEnvironment) {
        this.processEnvironment = processEnvironment;
    }

    /**
     * Set searchBeanFactory
     *
     * @param searchBeanFactory
     *            SearchBeanFactory.
     */
    public void setSearchBeanFactory(SearchBeanFactory searchBeanFactory) {
        this.searchBeanFactory = searchBeanFactory;
    }

    /**
     * Set searchService
     *
     * @param searchService
     *            SearchService.
     */
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param tokenRepository JAVADOC.
     */
    @Required
    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Return collection of those transitions which can be executed within the
     * workflow definition. The collection will include transactions not
     * protected by this privilege specified as well as all transactions without
     * any privilege.
     *
     * @param workflowId
     *            workflow definition id.
     * @param privilege
     *            privilege to check against.
     *
     * @return JAVADOC.
     * TODO add access control for the current user
     */
    @Override
    public Collection<Transition> listActionableTransitions(String workflowId) {
        ProcessDefinition workflowDefinition = processEnvironment.getDefinitionRegistry()
                                                                 .findWorkflowDefinition(workflowId);

        if (workflowDefinition == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("No workflow found for workflowId [" + workflowId + "], returning");
            }

            return Collections.emptySet();
        }

        String systemPrivilege = (accessRegistry.getSystemPrivilege() == null) ? null
                                                                               : accessRegistry.getSystemPrivilege()
                                                                                               .getName();
        Collection<Transition> ret = new HashSet<Transition>();
        State[] allPlaces = workflowDefinition.getAllPlaces();

        for (int i = 0; i < allPlaces.length; i++) {
            State place = allPlaces[i];

            if (place instanceof Station) {
                Station state = (Station) place;
                Collection<Transition> transitions = new HashSet<Transition>(state.getAllTransitions());

                if (!StringUtils.isEmpty(systemPrivilege)) {
                    for (Transition transition : transitions) {
                        if (transition.getPrivilegeNames() == null) {
                            // not protected transition
                            ret.add(transition);

                            continue;
                        }

                        if (transition.getPrivilegeNames().contains(systemPrivilege)) {
                            // skip protected by named privilege
                            continue;
                        }

                        ret.add(transition);
                    }
                } else {
                    ret.addAll(transitions);
                }
            }
        }

        return ret;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param ids
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional
    public Map<Long, Collection<String>> listAllTransitions(Collection<Long> ids,
        String applicationType) {
        Collection<ProcessToken> tokens = tokenRepository.findByApplicationTypeAndIds(applicationType,
                ids.toArray(new Long[ids.size()]));

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded " + tokens.size() + " for ids " + ids);
        }

        Map<Long, Collection<String>> results = new HashMap<Long, Collection<String>>();

        if (CollectionUtils.isNotEmpty(tokens)) {
            for (ProcessToken token : tokens) {
                results.put(token.getDomainObjectId(),
                    processEnvironment.getService().listTransitions(token, null));
            }
        }

        return results;
    }

    /**
     * List transitions for all objects. Assumption is that these objects are in
     * the same workflow place.
     *
     * @param ids
     *            Collection of ids for homogeneous objects.
     * @param applicationType
     *            type of the objects.
     *
     * @return Collection of available transitions.
     */
    @Override
    @Transactional
    public Collection<String> listTransitions(Collection<Long> ids, String applicationType) {
        Token token = loadLastToken(ids, applicationType);

        return processEnvironment.getService().listTransitions(token, null);
    }

    /**
     * Search considering this user's permissions
     *
     * @param ids
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional
    public Collection<Map<String, Object>> listWorkflowProperties(Collection<Long> ids,
        String applicationType) {
        if (searchService == null) {
            LOG.info("SearchService is null, no results for listWorkflowProperties");

            return Collections.emptyList();
        }

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", ids);

        SearchBean searchBean = searchBeanFactory.buildSearchBean(applicationType, params);

        searchBeanFactory.addProjections(applicationType, searchBean);

        params.put(PersistableEntity.APPLICATION_TYPE, applicationType);

        SearchResults searchResults = searchService.search(searchBean, params);
        List<Map<String, Object>> results = searchResults.searchMap(0, Integer.MAX_VALUE);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Returning [" + results.size() + "] for ids [" + ids.size() + "]");
        }

        return results;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Map<String, String>> loadTransitionInfo(String workflowId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading workflow states by transitionId for workflow [" + workflowId + "]");
        }

        ProcessDefinition wflDefinition = definitionService.loadDefinition(workflowId);

        Assert.notNull(wflDefinition, "wflDefinition should be found with id [" + workflowId + "]");

        Collection<Map<String, String>> stateTransitionInfo = new HashSet<Map<String, String>>();

        State[] places = wflDefinition.getAllPlaces();

        if (ArrayUtils.isNotEmpty(places)) {
            for (int i = 0; i < places.length; i++) {
                State place = places[i];

                if (CollectionUtils.isNotEmpty(place.getAllTransitions())) {
                    for (Transition transition : place.getAllTransitions()) {
                        Map<String, String> stateTransitionMap = new HashMap<String, String>();

                        stateTransitionMap.put("transition", transition.getId());
                        stateTransitionMap.put("stateFrom", transition.getInput().getId());
                        stateTransitionMap.put("stateTo", transition.getOutput().getId());
                        stateTransitionInfo.add(stateTransitionMap);
                    }
                }
            }
        }

        return stateTransitionInfo;
    }

    /**
     * Makes bulk transitions
     *
     * @param entities
     *            map of ids vs versions.
     * @param applicationType
     *            JAVADOC.
     * @param transitionId
     *            JAVADOC.
     * @param comment
     *            JAVADOC.
     * @param approvedAs
     *            JAVADOC.
     * @param assignedTo
     *            JAVADOC.
     * @param extraParams
     *            JAVADOC.
     * @param reason
     *            JAVADOC.
     * @param attachment
     *            JAVADOC.
     * @throws OptimisticLockException
     *             if any of versions in entities map are lower than the ones in
     *             the db.
     */
    @Override
    @Transactional
    public void makeTransition(Map<Long, Integer> entities, String applicationType,
        String transitionId, String comment, String approvedAs, String assignedTo,
        Map<String, Object> extraParams, ListNodeDto reason, Attachment attachment) {
        try {
            Collection<ProcessToken> tokens = tokenRepository.findByApplicationTypeAndIds(applicationType,
                    entities.keySet().toArray(new Long[entities.size()]));

            Assert.notEmpty(tokens, "Null tokens");

            for (ProcessToken token : tokens) {
                Integer version = entities.get(token.getDomainObjectId());

                if (version < token.getVersion()) {
                    throw new OptimisticLockException(
                        "Attempted to update outdated version of object id:" +
                        token.getDomainObjectId());
                }

                Map<String, Object> parameters = new HashMap<String, Object>();

                parameters.put(HistoryListener.APPROVEDBY_PROPERTY, approvedAs);
                parameters.put(HistoryListener.ASSIGNEDTO_PROPERTY, assignedTo);
                parameters.put(HistoryRecord.COMMENTS_PROPERTY, comment);
                parameters.put(HistoryListener.REASON_PROPERTY, reason);

                if (attachment != null) {
                    // Clone the attachment so each HistoryRecord has it's own
                    // instance.
                    try {
                        parameters.put(HistoryListener.ATTACHMENT_PROPERTY,
                            BeanUtils.cloneBean(attachment));
                    } catch (Exception e) {
                        throw new RuntimeException("Could not clone attachment", e);
                    }
                }

                parameters.put("extraParams", extraParams);

                token = (ProcessToken) processEnvironment.getService()
                                                         .executeTransition(token, transitionId,
                        parameters);

                if (processEnvironment.getProcessDefinitionHelper().isEnded(token)) {
                    tokenRepository.delete(token);
                } else {
                    tokenRepository.save(token);
                }
            }
        } catch (RuntimeException e) {
            // TODO is it correct to fail the batch is one item has failed?
            LOG.error("Oops", e);
            throw e;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     * @param transitionId
     *            JAVADOC.
     * @param comment
     *            JAVADOC.
     * @param approvedAs
     *            JAVADOC.
     * @param assignedTo
     *            JAVADOC.
     * @param extraParams
     *            JAVADOC.
     * @param attachment
     *            JAVADOC.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void makeTransition(Long id, String applicationType, String transitionId,
        String comment, String approvedAs, String assignedTo, Map<String, Object> extraParams,
        Attachment attachment) {
        try {
            Collection<ProcessToken> tokens = tokenRepository.findByApplicationTypeAndIds(applicationType,
                    id);

            Assert.notEmpty(tokens, "No tokens");

            ProcessToken token = tokens.iterator().next();

            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put(HistoryListener.APPROVEDBY_PROPERTY, approvedAs);
            parameters.put(HistoryListener.ASSIGNEDTO_PROPERTY, assignedTo);
            parameters.put(HistoryRecord.COMMENTS_PROPERTY, comment);
            parameters.put(HistoryListener.ATTACHMENT_PROPERTY, attachment);
            parameters.put("extraParams", extraParams);

            token = (ProcessToken) processEnvironment.getService()
                                                     .executeTransition(token, transitionId,
                    parameters);

            if (processEnvironment.getProcessDefinitionHelper().isEnded(token)) {
                tokenRepository.delete(token);
            } else {
                tokenRepository.save(token);
            }
        } catch (RuntimeException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Oops", e);
            }

            throw e;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param entity
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public List<HistoryRecord> obtainHistory(Long id, String applicationType) {
        return tokenRepository.findHistoryRecordsByDomainObjectIdAndDomainObjectType(id,
            applicationType);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<Object, Object>> obtainHistorySummary(Long id, String applicationType) {
        Assert.notNull(id, "id must be provided!");
        Assert.hasText(applicationType, "applicationType is required!");

        List<Map<Object, Object>> results = new LinkedList<Map<Object, Object>>();
        List<HistoryRecord> hres = historyRecordRepository.findByIdAndApplicationType(id,
                applicationType);

        for (HistoryRecord historyRecord : hres) {
            Map<Object, Object> bm = new BeanMap(historyRecord);

            results.add(bm);
        }

        return results;
    }

    /**
     * Creates and starts a workflow for it if one exists.
     *
     * @param entity
     *            PersistableEntity.
     * @param parameters
     *            Map<String, Object>.
     *
     * @return JAVADOC.
     */
    @Override
    public Token startWorkflow(PersistableEntity entity, Map<String, Object> parameters) {
        Assert.notNull(entity, "entity must be provided as a parameter");

        return startWorkflow(entity, entity.getApplicationType(), parameters);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param entity
     *            PersistableEntity.
     * @param processId
     *            JAVADOC.
     * @param parameters
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Token startWorkflow(PersistableEntity entity, String processId,
        Map<String, Object> parameters) {
        Assert.notNull(entity, "entity must be provided as a parameter");
        Assert.hasText(processId, "processId cannot be empty");

        ProcessToken token = (ProcessToken) processEnvironment.getService()
                                                              .startProcess(entity, processId,
                null, parameters);

        if (token == null) {
            LOG.warn("Failed to start a process for object " + entity);

            return null;
        }

        tokenRepository.save(token);

        return token;
    }

    private Token loadLastToken(Collection<Long> ids, String applicationType) {
        Assert.notNull(ids, "Ids is null");

        Collection<ProcessToken> tokens = tokenRepository.findByApplicationTypeAndIds(applicationType,
                ids.toArray(new Long[ids.size()]));

        Assert.notNull(tokens, "Failed to load tokens for ids " + ids);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded " + tokens.size() + " for ids " + ids);
        }

        String placeId = null;
        Token token = null;

        for (Token tokenItem : tokens) {
            Assert.notNull(tokenItem.getPlaceId(), "Token does not have a placeId");

            if (placeId == null) {
                placeId = tokenItem.getPlaceId();
            }

            if (tokenItem.getPlaceId().equals(placeId)) {
                token = tokenItem;

                continue;
            }

            throw new IllegalArgumentException(
                "Not all entities are at the same workflow state for bulk transition");
        }

        return token;
    }
}
