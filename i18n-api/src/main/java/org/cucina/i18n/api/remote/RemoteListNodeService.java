package org.cucina.i18n.api.remote;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.cucina.i18n.api.ListNodeDto;
import org.cucina.i18n.api.ListNodeService;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class RemoteListNodeService
    implements ListNodeService {
    private RestTemplate restTemplate = new RestTemplate();
    private String url;

    /**
     * Creates a new RemoteListNodeService object.
     *
     * @param url JAVADOC.
     */
    public RemoteListNodeService(String url) {
        this.url = url;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     * TODO add error handling
     */
    @Override
    public ListNodeDto load(Long id) {
        return restTemplate.getForObject(url + "/{id}", ListNodeDto.class, id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<ListNodeDto> loadByType(String type) {
        return restTemplate.getForObject(url + "/type/{type}", Collection.class, type);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNodeDto JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Long save(ListNodeDto listNodeDto) {
        ResponseEntity<Long> rid = restTemplate.postForEntity(url, listNodeDto, Long.class);

        return rid.getBody();
    }
}
