package org.cucina.sample.engine.client.app;

import org.cucina.engine.client.ProcessEngineFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;


/**
 * @author vlevine
 */
@Service
public class ClientServiceImpl
		implements ClientService {
	private static final Logger LOG = LoggerFactory.getLogger(ClientServiceImpl.class);
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ProcessEngineFacade processEngineFacade;

	/**
	 * @return .
	 */
	@Override
	@Transactional
	public Item create() {
		Item item = new Item();

		itemRepository.save(item);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Starting for Item " + item.getId());
		}

		Assert.isTrue(processEngineFacade.startWorkflow(Item.class.getSimpleName(),
				item.getId().toString(), null), "Failed to start process");

		return item;
	}

	@Override
	public Collection<Map<String, String>> loadTransitionInfo(String processName) {
		return processEngineFacade.loadTransitionInfo(processName);
	}
}
