package org.cucina.engine.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cucina.engine.server.communication.HistoryRecordDto;


/**
 *
 *
 * @author vlevine
  */
public interface ProcessEngineFacade {
    /**
     *
     *
     * @param ids .
     * @param applicationType .
     *
     * @return .
     */
    Collection<String> listTransitions(Collection<Long> ids, String applicationType);

    /**
     *
     *
     * @param ids .
     * @param applicationType .
     *
     * @return .
     */
    Collection<Map<String, Object>> listWorkflowProperties(Collection<Long> ids,
        String applicationType);

    /**
     *
     *
     * @param workflowId .
     *
     * @return .
     */
    Collection<Map<String, String>> loadTransitionInfo(String workflowId);

    /**
     *
     *
     * @param id .
     * @param applicationType .
     * @param transitionId .
     * @param comment .
     * @param approvedAs .
     * @param assignedTo .
     * @param extraParams .
     * @param attachment .
     */
    void makeTransition(String entityType, Long id, String transitionId, String comment,
        String approvedAs, String assignedTo, Map<String, Object> extraParams, Object attachment);

    /**
     *
     *
     * @param entities .
     * @param applicationType .
     * @param transitionId .
     * @param comment .
     * @param approvedAs .
     * @param assignedTo .
     * @param extraParams .
     * @param reason .
     * @param attachment .
     */
    void bulkTransition(Map<Long, Integer> entities, String entityType, String transitionId,
        String comment, String approvedAs, String assignedTo, Map<String, Object> extraParams,
        String reason, Object attachment);

    /**
     *
     *
     * @param id .
     * @param applicationType .
     *
     * @return .
     */
    List<HistoryRecordDto> obtainHistory(Long id, String applicationType);

    /**
     *
     *
     * @param id .
     * @param applicationType .
     *
     * @return .
     */
    List<Map<String, Object>> obtainHistorySummary(Long id, String applicationType);

    /**
     *
     *
     * @param id .
     * @param entityType .
     * @param workflowId .
     * @param parameters .
     *
     * @return .
     */
    boolean startWorkflow(String entityType, Long id, String workflowId,
        Map<String, Object> parameters);

    /**
     *
     *
     * @param id .
     * @param entityType .
     * @param parameters .
     *
     * @return .
     */
    boolean startWorkflow(String entityType, Long id, Map<String, Object> parameters);
}
