package org.cucina.sample.engine.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.engine.client.ProcessEngineFacade;


/**
 *
 *
 * @author vlevine
  */
@RestController
@RequestMapping("/client")
public class ClientController {
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
        Assert.isTrue(processEngineFacade.startWorkflow(Item.class.getSimpleName(),
                item.getId().longValue(), null), "Failed to start process");

        return item;
    }
}
