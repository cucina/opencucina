package org.cucina.security.service;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CurrentUserSetAdvice
    implements MethodBeforeAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserSetAdvice.class);
    private SystemUserService systemUserService;
    private UserAccessor userAccessor;

    /**
     * Creates a new CurrentUserSetInterceptor object.
     *
     * @param authenticationService JAVADOC.
     * @param userAccessor JAVADOC.
     */
    public CurrentUserSetAdvice(SystemUserService systemUserService, UserAccessor userAccessor) {
        Assert.notNull(systemUserService, "systemUserService is null");
        this.systemUserService = systemUserService;
        Assert.notNull(userAccessor, "userAccessor is null");
        this.userAccessor = userAccessor;
    }

    /**
      * JAVADOC Method Level Comments
      *
      * @param method JAVADOC.
      * @param arg1 JAVADOC.
      * @param arg2 JAVADOC.
      *
      * @throws Throwable JAVADOC.
      */
    @Override
    public void before(Method method, Object[] arg1, Object arg2)
        throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Called before method:" + method);
        }

        if ((method == null) || "toString".equals(method.getName())) {
            return;
        }

        UserDetails user = userAccessor.getCurrentUser();

        if (user == null) {
            String userName = systemUserService.getUsername();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempting to login for user:" + userName);
            }

            userAccessor.forceUserToContext(userName);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("There is a currently logged in user:" + user.getUsername());
            }
        }

        return;
    }
}
