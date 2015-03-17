package org.cucina.engine.server.repository;

import java.util.Collection;

import org.cucina.engine.model.WorkflowToken;
import org.cucina.engine.server.model.EntityDescriptor;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface EntityDescriptorRepository
    extends Repository<EntityDescriptor, Long> {
    /**
     * Returns names of workflows and number of active instances.
     *
     * @return JAVADOC.
     */
    Collection<Object[]> listAggregated();

    /**
     * JAVADOC Method Level Comments
     *
     * @param wfid JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<WorkflowToken> workflowSummary(String wfid);
}
