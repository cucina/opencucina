package org.cucina.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.security.access.AccessManager;
import org.cucina.security.access.AccessRegistry;
import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.PermissionDto;
import org.cucina.security.service.PermissionService;
import org.cucina.security.service.UserAccessor;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@RestController
@RequestMapping(value = "/security")
public class SecurityController
    implements AccessFacade {
    @Autowired
    private AccessManager<?> accessManager;
    @Autowired
    private AccessRegistry accessRegistry;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserAccessor userAccessor;

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/defaultPrivilege")
    public String getDefaultPrivilege() {
        return accessRegistry.getDefaultPrivilege().getName();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/systemPrivilege")
    public String getSystemPrivilege() {
        return accessRegistry.getSystemPrivilege().getName();
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
    @RequestMapping(method = RequestMethod.POST, value = "/hasPerimissions/{username}/{privilege}/{applicationType}")
    public boolean hasPermissions(@PathVariable
    String username, @PathVariable
    String privilege, @PathVariable
    String applicationType, Map<String, Object> properties) {
        userAccessor.forceUserToContext(username);

        return accessManager.hasPermission(privilege, applicationType, properties);
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
    @RequestMapping(method = RequestMethod.GET, value = "/hasPrivilege/{username}/{privilege}")
    public boolean hasPrivilege(@PathVariable
    String username, @PathVariable
    String privilege) {
        userAccessor.forceUserToContext(username);

        return accessManager.hasPrivilege(privilege);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
     * @param privilege
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/permission/{username}/{privilege}")
    public Collection<PermissionDto> permissionsByUserAndPrivilege(@PathVariable
    String username, @PathVariable
    String privilege) {
        return permissionService.loadByUserAndPrivilege(username, privilege);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     * @param accessLevel
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/permission/{username}/{applicationType}/{accessLevel}")
    public Collection<PermissionDto> permissionsByUserTypeAccessLevel(
        @PathVariable
    String username, @PathVariable
    String applicationType, @PathVariable
    String accessLevel) {
        return permissionService.loadByUserTypeAccessLevel(username, applicationType, accessLevel);
    }
}
