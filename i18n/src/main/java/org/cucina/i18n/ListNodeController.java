package org.cucina.i18n;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.i18n.api.ListNodeDto;
import org.cucina.i18n.api.ListNodeService;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@RestController
@RequestMapping(value = "/listNode")
public class ListNodeController {
    @Autowired
    private ListNodeService listNodeService;

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ListNodeDto getById(@PathVariable
    Long id) {
        Assert.notNull(id, "id is null");

        return listNodeService.load(id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/type/{type}")
    public Collection<ListNodeDto> getByType(@PathVariable
    String type) {
        return listNodeService.loadByType(type);
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
    @Valid
    ListNodeDto listNode) {
        return listNodeService.save(listNode);
    }
}
