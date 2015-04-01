package org.cucina.i18n.api.remote;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.api.ListItemService;


/**
 * A simple facade to the remote microservice.
 *
 * @author vlevine
 */
public class RemoteListNodeService
    implements ListItemService {
    private RestTemplate restTemplate = new RestTemplate();
    private String url;

    /**
     * Creates a new RemoteListNodeService object.
     *
     * @param url .
     */
    public RemoteListNodeService(String url) {
        Assert.hasText(url, "url is empty");
        this.url = url;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     *
     * @return JAVADOC. TODO add error handling
     */
    @Override
    public ListItemDto load(Long id) {
        return restTemplate.getForObject(url + "/{id}", ListItemDto.class, id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<ListItemDto> loadByType(String type) {
        return restTemplate.getForObject(url + "/type/{type}", Collection.class, type);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNodeDto
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Long save(ListItemDto listNodeDto) {
        ResponseEntity<Long> rid = restTemplate.postForEntity(url, listNodeDto, Long.class);

        return rid.getBody();
    }
}
