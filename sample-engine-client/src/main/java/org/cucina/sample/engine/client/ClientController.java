package org.cucina.sample.engine.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.engine.client.ProcessEngineFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
  */
@RestController
@RequestMapping("/client")
public class ClientController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ProcessEngineFacade processEngineFacade;

    /**
     *
     */
    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "/create")
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
}
