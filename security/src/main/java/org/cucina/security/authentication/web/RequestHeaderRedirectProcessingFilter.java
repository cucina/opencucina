
package org.cucina.security.authentication.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RequestHeaderRedirectProcessingFilter
    extends AbstractRedirectPreAuthenticatedProcessingFilter {
    private String credentialsRequestHeader;
    private String principalRequestHeader = "SM_USER";
    private boolean exceptionIfHeaderMissing = true;

    /**
     * Creates a new RequestHeaderRedirectProcessingFilter object.
     *
     * @param notUsed JAVADOC.
     */
    public RequestHeaderRedirectProcessingFilter(String notUsed) {
        super(notUsed);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param credentialsRequestHeader JAVADOC.
     */
    public void setCredentialsRequestHeader(String credentialsRequestHeader) {
        Assert.hasText(credentialsRequestHeader,
            "credentialsRequestHeader must not be empty or null");
        this.credentialsRequestHeader = credentialsRequestHeader;
    }

    /**
     * Defines whether an exception should be raised if the principal header is missing. Defaults to {@code true}.
     *
     * @param exceptionIfHeaderMissing set to {@code false} to override the default behaviour and allow
     *          the request to proceed if no header is found.
     */
    public void setExceptionIfHeaderMissing(boolean exceptionIfHeaderMissing) {
        this.exceptionIfHeaderMissing = exceptionIfHeaderMissing;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param principalRequestHeader JAVADOC.
     */
    public void setPrincipalRequestHeader(String principalRequestHeader) {
        Assert.hasText(principalRequestHeader, "principalRequestHeader must not be empty or null");
        this.principalRequestHeader = principalRequestHeader;
    }

    /**
     * Credentials aren't usually applicable, but if a {@code credentialsRequestHeader} is set, this
     * will be read and used as the credentials value. Otherwise a dummy value will be used.
     */
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        if (credentialsRequestHeader != null) {
            return request.getHeader(credentialsRequestHeader);
        }

        return "N/A";
    }

    /**
     * Read and returns the header named by {@code principalRequestHeader} from the request.
     *
     * @throws PreAuthenticatedCredentialsNotFoundException if the header is missing and {@code exceptionIfHeaderMissing}
     *          is set to {@code true}.
     */
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String principal = request.getHeader(principalRequestHeader);

        if ((principal == null) && exceptionIfHeaderMissing) {
            throw new PreAuthenticatedCredentialsNotFoundException(principalRequestHeader +
                " header not found in request.");
        }

        return principal;
    }
}
