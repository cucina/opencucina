
package org.cucina.engine.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.cucina.engine.service.ProcessSupportService;
import org.cucina.search.AbstractResultSetModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Adds transitionIds to results with no permission checks.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UnrestrictedTransitionsAppender
    extends AbstractResultSetModifier {
    private static final Logger LOG = LoggerFactory.getLogger(UnrestrictedTransitionsAppender.class);

    /** This is a field JAVADOC */
    public static final String PROPERTY_NAME = "transitionIds";
    private ProcessSupportService workflowSupportService;

    /**
     * Creates a new UnrestrictedTransitionsAppender object.
     *
     * @param workflowSupportService JAVADOC.
     */
    public UnrestrictedTransitionsAppender(ProcessSupportService workflowSupportService) {
        super();
        Assert.notNull(workflowSupportService, "workflowSupportService cannot be null");
        this.workflowSupportService = workflowSupportService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType String.
     * @param results Collection<Map<String, Object>> results.
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

        Map<Long, Collection<String>> transitionsById = workflowSupportService.listAllTransitions(new HashSet<Long>(
                    resultsById.keySet()), applicationType);

        if (MapUtils.isEmpty(transitionsById)) {
            LOG.debug("NO transitions returned");

            return;
        }

        for (Map.Entry<Long, Collection<String>> entry : transitionsById.entrySet()) {
            Map<String, Object> result = resultsById.get(entry.getKey());

            if (result == null) {
                LOG.warn("Failed to find result with id=" + entry.getKey());

                continue;
            }

            result.put(PROPERTY_NAME, entry.getValue());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Added transitions [" + entry.getValue() + "] for object [" +
                    entry.getKey() + "] of type [" + applicationType + "]");
            }
        }
    }

    /**
    * The property that will be added
    *
    * @param applicationType String.
    *
    * @return list of properties.
    */
    @Override
    public Collection<String> listProperties(String applicationType) {
        return Arrays.asList(PROPERTY_NAME);
    }
}
