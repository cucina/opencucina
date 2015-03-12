
package org.cucina.engine.security;

import java.util.Collection;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.security.access.AbstractSecuredTypeRegistry;
import org.springframework.beans.factory.annotation.Required;


/**
 * Simple <code>SecuredTypeRegistry</code> which just returns the types
 * which are workflowed.
 * @author hkelsey
 *
 */
public class WorkflowSecuredTypeRegistry
    extends AbstractSecuredTypeRegistry {
    private ProcessEnvironment workflowEnvironment;

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowEnvironment JAVADOC.
     */
    @Required
    public void setWorkflowEnvironment(ProcessEnvironment workflowEnvironment) {
        this.workflowEnvironment = workflowEnvironment;
    }

    /**
     * Returns the workflow types.
     */
    @Override
    public Collection<String> listSecuredTypes() {
        return workflowEnvironment.listWorkflows();
    }
}
