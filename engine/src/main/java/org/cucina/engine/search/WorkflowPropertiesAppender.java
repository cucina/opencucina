
package org.cucina.engine.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.engine.service.ProcessSupportService;
import org.cucina.search.AbstractResultSetModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowPropertiesAppender
    extends AbstractResultSetModifier {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowPropertiesAppender.class);

    /** This is a field JAVADOC */
    public static final String PROPERTY_NAME = "workflowProperties";
    private ProcessSupportService workflowSupportService;

    /**
     * Creates a new WorkflowPropertiesAppender object.
     *
     * @param workflowSupportService JAVADOC.
     */
    public WorkflowPropertiesAppender(ProcessSupportService workflowSupportService) {
        super();
        Assert.notNull(workflowSupportService,
            "workflowSupportService must be provided as an argument");
        this.workflowSupportService = workflowSupportService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param results JAVADOC.
     */
    @Override
    public void doModify(String applicationType, Collection<Map<String, Object>> results) {
        if (CollectionUtils.isEmpty(results)) {
            LOG.debug("Empty results, not processing and returning");

            return;
        }

        Map<Long, Map<String, Object>> resultsById = new HashMap<Long, Map<String, Object>>();

        for (Map<String, Object> result : results) {
            Long id = (Long) result.get("id");

            if (id != null) {
                resultsById.put(id, result);
            }
        }

        Collection<Map<String, Object>> workflowPropsById = workflowSupportService.listWorkflowProperties(new HashSet<Long>(
                    resultsById.keySet()), applicationType);

        if (CollectionUtils.isEmpty(workflowPropsById)) {
            LOG.warn("NO workflow properties returned");

            return;
        }

        for (Map<String, Object> workflowProps : workflowPropsById) {
            Object domainObj = workflowProps.get("id");

            if (domainObj == null) {
                LOG.warn("Unexpected, no domainObjectId returned in results [" + workflowProps +
                    "]");

                continue;
            }

            if (domainObj instanceof Number) {
                Long domainObjectId = ((Number) domainObj).longValue();
                Map<String, Object> result = resultsById.get(domainObjectId);

                if (result == null) {
                    LOG.warn("Unexpected, domainObjectId returned which has no existing result [" +
                        workflowProps + "]");

                    continue;
                }

                result.putAll(workflowProps);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Added workflowProperties [" + workflowProps + "] for object [" +
                        domainObjectId + "] of type [" + applicationType + "]");
                }
            }
        }
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param applicationType JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    public Collection<String> listProperties(String applicationType) {
        return Arrays.asList(PROPERTY_NAME);
    }
}
