package org.cucina.engine.server.web.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.server.repository.EntityDescriptorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Controller
public class WorkflowViewWebService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowViewWebService.class);
    private static final String[] summaryColumns = {
            "workflowDefinitionId", "placeId", "domainObjectType", "domainObjectId",
            "domainObject.applicationName"
        };
    private EntityDescriptorRepository entityDescriptorRepository;

    /**
     * Creates a new WorkflowViewService object.
     *
     * @param workflowRepository
     *            JAVADOC.
     */
    @Autowired
    public WorkflowViewWebService(EntityDescriptorRepository entityDescriptorRepository) {
        Assert.notNull(entityDescriptorRepository, "entityDescriptorRepository is null");
        this.entityDescriptorRepository = entityDescriptorRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @RequestMapping(value = "/workflow", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Object[]> listWorkflows() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("listWorkflows called");
        }

        return entityDescriptorRepository.listAggregated();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param wfid
     *
     *
     * @return JAVADOC.
     */
    @RequestMapping(value = "/workflow/{wfid}", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Object[]> workflowSummary(@PathVariable
    String wfid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("workflowSummary called with " + wfid);
        }

        Collection<ProcessToken> tokens = entityDescriptorRepository.workflowSummary(wfid);
        Collection<Object[]> result = new ArrayList<Object[]>();

        for (ProcessToken workflowToken : tokens) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(workflowToken);
            Object[] line = new Object[summaryColumns.length];

            for (int i = 0; i < summaryColumns.length; i++) {
                line[i] = beanWrapper.getPropertyValue(summaryColumns[i]);
            }

            result.add(line);
        }

        return result;
    }
}
