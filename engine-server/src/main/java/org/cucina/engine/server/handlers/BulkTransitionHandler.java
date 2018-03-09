package org.cucina.engine.server.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.engine.server.event.BulkTransitionEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.api.ListItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;


/**
 * @author vlevine
 */
@Component
public class BulkTransitionHandler
		extends AbstractProcessEventHandler<BulkTransitionEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(BulkTransitionHandler.class);
	private ListItemService listItemService;

	/**
	 * Creates a new StartWorkflowEventHandler object.
	 *
	 * @param processSupportService .
	 */
	@Autowired
	public BulkTransitionHandler(ProcessSupportService processSupportService,
								 ListItemService listItemService) {
		super(processSupportService);
		Assert.notNull(listItemService, "listItemService is null");
		this.listItemService = listItemService;
	}

	/**
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
