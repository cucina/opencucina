
package org.cucina.security.authentication.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ExceptionMappingMessageCodeFailureHandler
    implements AuthenticationFailureHandler {
    /** This is a field JAVADOC */
    public static final String AUTHENTICATION_MESSAGE_CODE_KEY = "AUTHENTICATION_MESSAGE_CODE";
    protected final Logger LOG = LoggerFactory.getLogger(ExceptionMappingMessageCodeFailureHandler.class);
    private final Map<String, String> failureCodeMap = new HashMap<String, String>();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private String defaultFailureUrl;
    private String defaultMessageCode;
    private boolean allowSessionCreation = true;
    private boolean forwardToDestination = false;

    /**
     * Creates a new ExceptionMappingMessageCodeFailureHandler object.
     */
    public ExceptionMappingMessageCodeFailureHandler() {
    }

    /**
     * Creates a new ExceptionMappingMessageCodeFailureHandler object.
     *
     * @param defaultFailureUrl JAVADOC.
     * @param defaultMessageCode JAVADOC.
     */
    public ExceptionMappingMessageCodeFailureHandler(String defaultFailureUrl,
        String defaultMessageCode) {
        setDefaultFailureUrl(defaultFailureUrl);
        setDefaultMessageCode(defaultMessageCode);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param allowSessionCreation JAVADOC.
     */
    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
    }

    /**
     * The URL which will be used as the failure destination.
     *
     * @param defaultFailureUrl the failure URL, for example "/loginFailed.jsp".
     */
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
            "'" + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param defaultMessageCode JAVADOC.
     */
    public void setDefaultMessageCode(String defaultMessageCode) {
        this.defaultMessageCode = defaultMessageCode;
    }

    /**
     * Sets the map of exception types (by name) to URLs.
     *
     * @param failureCodeMap the map keyed by the fully-qualified name of the exception class, with the corresponding
     * failure URL as the value.
     *
     * @throws IllegalArgumentException if the entries are not Strings or the URL is not valid.
     */
    public void setExceptionMappings(Map<?, ?> failureCodeMap) {
        this.failureCodeMap.clear();

        for (Map.Entry<?, ?> entry : failureCodeMap.entrySet()) {
            Object exception = entry.getKey();
            Object messageCode = entry.getValue();

            Assert.isInstanceOf(String.class, exception,
                "Exception key must be a String (the exception classname).");
            Assert.isInstanceOf(String.class, messageCode, "message code must be a String");
            this.failureCodeMap.put((String) exception, (String) messageCode);
        }
    }

    /**
     * Allows overriding of the behaviour when redirecting to a target URL.
     */
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    /**
     * If set to <tt>true</tt>, performs a forward to the failure destination URL instead of a redirect. Defaults to
     * <tt>false</tt>.
     */
    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    /**
    * Performs the redirect or forward to the {@code defaultFailureUrl} if set, otherwise returns a 401 error code.
    * <p>
    * If redirecting or forwarding, {@code saveException} will be called to cache the exception for use in
    * the target view.
    */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception)
        throws IOException, ServletException {
        if (defaultFailureUrl == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No failure URL set, sending 401 Unauthorized error");
            }

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Authentication Failed: " + exception.getMessage());
        } else {
            saveException(request, exception);

            if (forwardToDestination) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Forwarding to " + defaultFailureUrl);
                }

                request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Redirecting to " + defaultFailureUrl);
                }

                redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected boolean isAllowSessionCreation() {
        return allowSessionCreation;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected boolean isUseForward() {
        return forwardToDestination;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param exception JAVADOC.
     *
     * @return JAVADOC.
     */
    protected final String deriveMessageCode(AuthenticationException exception) {
        String className = exception.getClass().getName();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting code for [" + className + "]");
        }

        String messageCode = failureCodeMap.get(className);

        if (StringUtils.isNotEmpty(messageCode)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found code for [" + className + "]:[" + messageCode + "]");
            }

            return messageCode;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Not found message code for className [" + className + "], defaulting to [" +
                defaultMessageCode + "]");
        }

        return defaultMessageCode;
    }

    /**
     * Caches the {@code AuthenticationException} for use in view rendering.
     * <p>
     * If {@code forwardToDestination} is set to true, request scope will be used, otherwise it will attempt to store
     * the exception in the session. If there is no session and {@code allowSessionCreation} is {@code true} a session
     * will be created. Otherwise the exception will not be stored.
     */
    protected final void saveException(HttpServletRequest request, AuthenticationException exception) {
        String messageCode = deriveMessageCode(exception);

        if (forwardToDestination) {
            request.setAttribute(AUTHENTICATION_MESSAGE_CODE_KEY, messageCode);
        } else {
            HttpSession session = request.getSession(false);

            if ((session != null) || allowSessionCreation) {
                request.getSession().setAttribute(AUTHENTICATION_MESSAGE_CODE_KEY, messageCode);
            }
        }
    }
}
