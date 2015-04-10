package org.cucina.sample.engine.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.sample.engine.client.app.ClientService;
import org.cucina.sample.engine.client.app.Item;

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
    private ClientService clientService;

    /**
    *
    */
    @RequestMapping(method = RequestMethod.GET, value = "/create")
    @Conversation
    public Item create() {
        LOG.trace("Stack:", new Exception("Ignore"));

        return clientService.create();
    }
}
