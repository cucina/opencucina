package org.cucina.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.repository.ListNodeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@RestController
@RequestMapping(value = "/listNode")
public class ListNodeController {
    private static final Logger LOG = LoggerFactory.getLogger(ListNodeController.class);
    @Autowired
    private ListNodeRepository listNodeRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ListNode getById(Long id) {
        return listNodeRepository.find(id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNode JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.POST)
    public Long save(@RequestBody
    ListNode listNode) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Before" + listNode);
        }

        listNodeRepository.save(listNode);

        if (LOG.isDebugEnabled()) {
            LOG.debug("After" + listNode);
        }

        return listNode.getId();
    }
}
