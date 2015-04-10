package org.cucina.engine.server.handlers;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.cucina.engine.server.event.workflow.BulkTransitionEvent;
import org.cucina.engine.service.ProcessSupportService;

import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.api.ListItemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
  */
@Component
public class BulkTransitionEventHandler
    extends AbstractProcessEventHandler<BulkTransitionEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(BulkTransitionEventHandler.class);
    private ListItemService listItemService;

    /**
     * Creates a new StartWorkflowEventHandler object.
     *
     * @param processSupportService .
     */
    @Autowired
    public BulkTransitionEventHandler(ProcessSupportService processSupportService,
        ListItemService listItemService) {
        super(processSupportService);
        Assert.notNull(listItemService, "listItemService is null");
        this.listItemService = listItemService;
    }

    /**
     *
     *
     * @param event .
     */
    @Override
    protected void doProcess(BulkTransitionEvent event) {
        Collection<ListItemDto> reasons = listItemService.loadByType(event.getReason());

        if (CollectionUtils.isEmpty(reasons)) {
            LOG.warn("Failed to load reason for string '" + event.getReason() + "'");

            throw new RuntimeException("Failed to load reason for string '" + event.getReason() +
                "'");
        }

        // TODO make a more intelligent choice
        ListItemDto reason = reasons.iterator().next();

        // TODO convert we.getAttachment() to an Attachment
        getProcessSupportService()
            .makeBulkTransition(event.getEntities(), event.getType(), event.getTransitionId(),
            event.getComment(), event.getApprovedAs(), event.getAssignedTo(),
            event.getExtraParams(), reason, null);
    }
}
