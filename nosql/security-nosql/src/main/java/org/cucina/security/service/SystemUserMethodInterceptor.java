package org.cucina.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.cucina.security.api.CurrentUserAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SystemUserMethodInterceptor
    implements MethodInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(SystemUserMethodInterceptor.class);
    private SystemUserService systemUserService;
    private UserAccessor userAccessor;

    /**
     * Creates a new SystemUserMethodInterceptor object.
     *
     *
     * @param userAccessor JAVADOC.
     * @param systemUserService JAVADOC.
     */
    public SystemUserMethodInterceptor(UserAccessor userAccessor,
        SystemUserService systemUserService) {
        Assert.notNull(userAccessor, "authenticationService is null");
        this.userAccessor = userAccessor;
        Assert.notNull(systemUserService, "userAccessor is null");
        this.systemUserService = systemUserService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0 JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws Throwable JAVADOC.
     */
    @Override
    public Object invoke(MethodInvocation arg0)
        throws Throwable {
        //get auth for current user
        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting current auth");
        }

        Authentication currentAuthentication = CurrentUserAccessor.currentAuthentication();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Current auth: " + currentAuthentication);
            LOG.debug("Getting username");
        }

        String username = systemUserService.getUsername();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Logging in system user with username [" + username + "]");
        }

        userAccessor.forceUserToContext(username);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Invoking method " + arg0.getMethod());
        }

        //invoke method
        Object ret = arg0.proceed();

        //switch back
        if (LOG.isDebugEnabled()) {
            LOG.debug("After call");
            LOG.debug("Switch back to " + currentAuthentication);
        }

        CurrentUserAccessor.switchAuthentication(currentAuthentication);

        return ret;
    }
}
