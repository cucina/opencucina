package org.cucina.engine.repository;

import java.util.Collection;

import org.springframework.data.repository.Repository;

import org.cucina.engine.model.Workflow;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface WorkflowRepository
    extends Repository<Workflow, Long> {
    /**
     * Determine if there is at least an instance of the named workflow
     * currently active
     *
     * @param definitionId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean isActive(String definitionId);

    /**
     *
     *
     * @param workflowId .
     */
    void delete(String workflowId);

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean exists(String definitionId);

    /**
     * List all workflows ids
     *
     * @return JAVADOC.
     */
    Collection<String> findAllIds();

    /**
     * Load all workflows
     *
     * @return JAVADOC.
     */
    Collection<Workflow> findAll();

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T>
     *            JAVADOC.
     * @param definitionId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Workflow findByWorkflowId(String definitionId);

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflow JAVADOC.
     */
    void save(Workflow workflow);
}
