package org.cucina.engine.repository;

import java.util.Collection;

import org.cucina.engine.model.Workflow;
import org.springframework.data.repository.Repository;


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
     * JAVADOC Method Level Comments
     *
     * @param workflow JAVADOC.
     */
    void save(Workflow workflow);

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
    Collection<String> listAll();

    /**
     * Load all workflows
     *
     * @return JAVADOC.
     */
    Collection<Workflow> loadAll();

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
    Workflow loadByWorkflowId(String definitionId);
}
