
package org.cucina.security.authentication.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class AbstractRedirectPreAuthenticatedProcessingFilter
    extends AbstractAuthenticationProcessingFilter {
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private boolean checkForPrincipalChanges;
    private boolean invalidateSessionOnPrincipalChange = true;

    /**
     * Creates a new AbstractRedirectPreAuthenticatedProcessingFilter object.
     *
     * @param notUsed JAVADOC.
     */
    protected AbstractRedirectPreAuthenticatedProcessingFilter(String notUsed) {
        super(notUsed);
    }

    /**
     * @param authenticationDetailsSource
     *            The AuthenticationDetailsSource to use
     */
    public void setAuthenticationDetailsSource(
        AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    /**
      * If set, the pre-authenticated principal will be checked on each request and compared
      * against the name of the current <tt>Authentication</tt> object. If a change is detected,
      * the user will be reauthenticated.
      *
      * @param checkForPrincipalChanges
      */
    public void setCheckForPrincipalChanges(boolean checkForPrincipalChanges) {
        this.checkForPrincipalChanges = checkForPrincipalChanges;
    }

    /**
     * If <tt>checkForPrincipalChanges</tt> is set, and a change of principal is detected, determines whether
     * any existing session should be invalidated before proceeding to authenticate the new principal.
     *
     * @param invalidateSessionOnPrincipalChange <tt>false</tt> to retain the existing session. Defaults to <tt>true</tt>.
     */
    public void setInvalidateSessionOnPrincipalChange(boolean invalidateSessionOnPrincipalChange) {
        this.invalidateSessionOnPrincipalChange = invalidateSessionOnPrincipalChange;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     * @param response JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws AuthenticationException JAVADOC.
     * @throws IOException JAVADOC.
     * @throws ServletException JAVADOC.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        Object principal = getPreAuthenticatedPrincipal(request);
        Object credentials = getPreAuthenticatedCredentials(request);

        if (principal == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No pre-authenticated principal found in request");
            }

            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("preAuthenticatedPrincipal = " + principal + ", trying to authenticate");
        }

        PreAuthenticatedAuthenticationToken authRequest = new PreAuthenticatedAuthenticationToken(principal,
                credentials);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
        return authenticationDetailsSource;
    }

    /**
     * Override to extract the credentials (if applicable) from the current request. Should not return null for a valid
     * principal, though some implementations may return a dummy value.
     */
    protected abstract Object getPreAuthenticatedCredentials(HttpServletRequest request);

    /**
     * Override to extract the principal information from the current request
     */
    protected abstract Object getPreAuthenticatedPrincipal(HttpServletRequest request);

    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     * @param response JAVADOC.
     *
     * @return JAVADOC.
     * @deprecated parent method is deprecated and has instruction on use.
     */
    @Override
    protected boolean requiresAuthentication(HttpServletRequest request,
        HttpServletResponse response) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            return true;
        }

        if (!checkForPrincipalChanges) {
            return false;
        }

        Object principal = getPreAuthenticatedPrincipal(request);

        if (currentUser.getName().equals(principal)) {
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Pre-authenticated principal has changed to " + principal +
                " and will be reauthenticated");
        }

        if (invalidateSessionOnPrincipalChange) {
            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);

            if (session != null) {
                logger.debug("Invalidating existing session");
                session.invalidate();
                request.getSession();
            }
        }

        return true;
    }
}
