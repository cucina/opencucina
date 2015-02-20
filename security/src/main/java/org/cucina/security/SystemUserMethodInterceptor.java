
package org.cucina.security;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.cucina.security.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SystemUserMethodInterceptor
    implements MethodInterceptor, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(SystemUserMethodInterceptor.class);
    private AuthenticationService authenticationService;
    private SystemUserService systemUserService;

    /**
     * Creates a new SystemUserMethodInterceptor object.
     *
     * @param authenticationService JAVADOC.
     * @param systemUserService JAVADOC.
     */
    public SystemUserMethodInterceptor(AuthenticationService authenticationService,
        SystemUserService systemUserService) {
        super();
        this.authenticationService = authenticationService;
        Assert.notNull(authenticationService);
        this.systemUserService = systemUserService;
        Assert.notNull(systemUserService);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        Assert.notNull(systemUserService);
        Assert.notNull(authenticationService);
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

        Authentication currentAuthentication = ContextUserAccessor.currentAuthentication();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Current auth: " + currentAuthentication);
            LOG.debug("Getting username");
        }

        String username = systemUserService.getUsername();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Logging in system user with username [" + username + "]");
        }

        authenticationService.forceUserToContext(username);

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

        ContextUserAccessor.switchAuthentication(currentAuthentication);

        return ret;
    }
}
