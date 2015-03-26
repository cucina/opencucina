
package org.cucina.engine.validation;

import java.util.Arrays;
import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.ArrayUtils;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.Assert;


/**
 * Validates that new workflow definition contains all places from the previous
 * version.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowPlacesRetainedValidator
    implements ConstraintValidator<WorkflowPlacesRetained, Workflow> {
    private ProcessEnvironment workflowEnvironment;

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     * @param context
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isValid(Workflow target, ConstraintValidatorContext context) {
        WorkflowHistory wflHistory = populateWfl(target.getLatestWorkflowHistory());
        WorkflowHistory previousHistory = populateWfl(target.getWorkflowHistories()
                                                            .get(target.getWorkflowHistories().size() -
                    2));

        State[] oldPlaces = previousHistory.getProcessDefinition().getAllPlaces();

        if (ArrayUtils.isEmpty(oldPlaces)) {
            return true;
        }

        Transformer transformer = new IdTransformer();
        Collection<String> oldNames = CollectionUtils.collect(Arrays.asList(oldPlaces), transformer);

        State[] newPlaces = wflHistory.getProcessDefinition().getAllPlaces();

        Collection<String> newNames = CollectionUtils.collect(Arrays.asList(newPlaces), transformer);

        return CollectionUtils.isSubCollection(oldNames, newNames);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowEnvironment
     *            JAVADOC.
     */
    @Autowired
    public void setWorkflowEnvironment(ProcessEnvironment workflowEnvironment) {
        this.workflowEnvironment = workflowEnvironment;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     */
    @Override
    public void initialize(WorkflowPlacesRetained arg0) {
    }

    private WorkflowHistory populateWfl(WorkflowHistory history) {
        Assert.notNull(history, "history is null");

        ProcessDefinition definition = history.getProcessDefinition();

        if (definition == null) {
            definition = workflowEnvironment.getDefinitionParser()
                                            .parse(new ByteArrayResource(
                        history.getAttachment().getData()));
            history.setProcessDefinition(definition);
        }

        return history;
    }

    private static final class IdTransformer
        implements Transformer {
        @Override
        public Object transform(Object arg0) {
            return ((State) arg0).getId();
        }
    }
}
