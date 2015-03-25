package org.cucina.security.api.remote;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.PermissionDto;


/**
 *
 *
 * @author vlevine
 */
public class RemoteAccessFacade
    implements AccessFacade {
    private RestTemplate restTemplate = new RestTemplate();
    private String url;

    /**
     * Creates a new RemoteAccessFacade object.
     *
     * @param url .
     */
    public RemoteAccessFacade(String url) {
        Assert.hasText(url, "url is empty");
        this.url = url;
    }

    /**
     *
     *
     * @return .
     */
    @Override
    public String getDefaultPrivilege() {
        return restTemplate.getForObject(url + "/defaultPrivilege", String.class);
    }

    /**
     *
     *
     * @return .
     */
    @Override
    public String getSystemPrivilege() {
        return restTemplate.getForObject(url + "/systemPrivilege", String.class);
    }

    /**
     *
     *
     * @param username
     *            .
     * @param privilege
     *            .
     * @param applicationType
     *            .
     * @param properties
     *            .
     *
     * @return .
     */
    @Override
    public boolean hasPermissions(String username, String privilege, String applicationType,
        Map<String, Object> properties) {
        return restTemplate.postForObject(url + "/hasPerimissions", properties, Boolean.class,
            username, privilege, applicationType);
    }

    /**
     *
     *
     * @param username
     *            .
     * @param privilege
     *            .
     *
     * @return .
     */
    @Override
    public boolean hasPrivilege(String username, String privilege) {
        return restTemplate.getForObject(url + "/hasPrivilege/{username}/{privilege}",
            Boolean.class, username, privilege);
    }

    /**
     *
     *
     * @param username
     *            .
     * @param privilege
     *            .
     *
     * @return .
     */
    @Override
    public Collection<PermissionDto> permissionsByUserAndPrivilege(String username, String privilege) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     *
     *
     * @param username
     *            .
     * @param applicationType
     *            .
     * @param accessLevel
     *            .
     *
     * @return .
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<PermissionDto> permissionsByUserTypeAccessLevel(String username,
        String applicationType, String accessLevel) {
        return restTemplate.getForObject(url +
            "/permission/{username}/{applicationType}/{accessLevel}", Collection.class, username,
            applicationType, accessLevel);
    }
}
