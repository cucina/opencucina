package org.cucina.engine.operations;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.cucina.email.api.EmailDto;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.Operation;
import org.cucina.engine.email.AcegiUserAccessorBean;
import org.cucina.engine.email.UserAccessorBean;
import org.cucina.engine.event.EmailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class Email Action. Creates {@link EmailEvent} according to the parameters
 * set.
 */
public class EmailOperation
    extends AbstractOperation
    implements ApplicationEventPublisherAware, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(EmailOperation.class);

    /** users. */
    public static final String USERS = "users";
    private ApplicationEventPublisher applicationEventPublisher;
    private Boolean filterCurrentUser = Boolean.TRUE;
    private String contextUsersKey = USERS;

    /** Sets email descriptor name-value pairs to the map's name-value pairs. */
    private String parameterMapExpr;
    private String propertiesList;
    private String templateName;
    private UserAccessorBean userAccessor;

    /**
     * applicationEventPublisher setter
     *
     * @param applicationEventPublisher.
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Set the key with which to extract the distribution list of Users.
     *
     * @param contextUsersKey
     *            JAVADOC.
     */
    public void setContextUsersKey(String contextUsersKey) {
        this.contextUsersKey = contextUsersKey;
    }

    /**
     * Set filterCurrentUser, whether to include the currently logged
     * in/authenticated User in the distribution list for email.
     *
     * @param filterCurrentUser
     *            The filterCurrentUser to set.
     */
    public void setFilterCurrentUser(Boolean filterCurrentUser) {
        this.filterCurrentUser = filterCurrentUser;
    }

    /**
     * The expression which evaluates to Map of parameters to provide for email.
     *
     * @param parameterMapExpr
     *            The parameterMapExpr to set.
     */
    public void setParameterMapExpr(String parameterMapExpr) {
        this.parameterMapExpr = parameterMapExpr;
    }

    /**
     * The comma separates list of Token properties to provide in parameters to
     * email.
     *
     * @param propertiesList
     *            String.
     */
    public void setPropertiesList(String propertiesList) {
        this.propertiesList = propertiesList;
    }

    /**
     * Name of the email template to be used to generate email content.
     *
     * @param templateName
     *            String.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Set userAccesor
     *
     * @param userAccessor
     *            engineUserAccessor
     */
    public void setUserAccessor(UserAccessorBean userAccessor) {
        this.userAccessor = userAccessor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        if (userAccessor == null) {
            userAccessor = new AcegiUserAccessorBean();
        }
    }

    /**
     * @see Operation#execute(ExecutionContext)
     */
    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext executionContext) {
        Assert.notNull(executionContext, "Workflow context cannot be null.");
        Assert.notNull(templateName, "template name cannot be null.");

        Set<String> users = new HashSet<String>();

        users.addAll(getContextUsers(executionContext));

        filterUsers(users);

        if (CollectionUtils.isEmpty(users)) {
            LOG.debug("No users to send email to");

            return;
        }

        EmailDto descriptor = new EmailDto();

        descriptor.setTo(StringUtils.join(users, ","));
        descriptor.setMessageKey(templateName);

        Map<String, Object> params = extractTokenParameters(executionContext, propertiesList);
        Map<String, Object> parameterMap = (Map<String, Object>) executionContext.getExpressionExecutor()
                                                                                 .evaluate(executionContext,
                parameterMapExpr);

        if (parameterMap != null) {
            params.putAll(parameterMap);
        }

        descriptor.setParameters(params);

        /*
                Collection<DataSource> attachments = (Collection<DataSource>) executionContext.getParameters()
                                                                                              .get(contextParamAttachmentsKey);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Added attachments [" + ((attachments == null) ? 0 : attachments.size()) +
                        "]");
                }

                descriptor.setAttachments(attachments);

                if (attachmentRequired && CollectionUtils.isEmpty(descriptor.getAttachments())) {
                    LOG.debug("Not sending email as attachments are required and there are none");

                    return;
                }
        */
        if (LOG.isDebugEnabled()) {
            LOG.debug("EmailDescriptor=" + descriptor);
        }

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Publishing EmailEvent [" + descriptor.toString() + "]");
            }

            // TODO use api provided
            applicationEventPublisher.publishEvent(new EmailEvent(descriptor));
        } catch (RuntimeException e) {
            LOG.error("Errow while trying to send email");
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<String> getContextUsers(ExecutionContext executionContext) {
        Collection<String> result = null;
        Map<String, Object> parameters = executionContext.getParameters();

        if (parameters != null) {
            result = (Collection<String>) parameters.get(contextUsersKey);

            if (result != null) {
                // Check whether any of the users are null and
                // remove them, if so
                Iterator<String> userIter = result.iterator();

                while (userIter.hasNext()) {
                    if (userIter.next() == null) {
                        userIter.remove();
                    }
                }
            }
        }

        if (result != null) {
            return result;
        }

        return Collections.emptySet();
    }

    /**
     * JAVADOC.
     *
     * @param token
     *            JAVADOC.
     * @param propertiesList
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    private Map<String, Object> extractTokenParameters(ExecutionContext executionContext,
        String propertiesList) {
        if (StringUtils.isEmpty(propertiesList)) {
            return new HashMap<String, Object>();
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        String[] properNames = propertiesList.split(",");

        for (int i = 0; i < properNames.length; i++) {
            String name = properNames[i].trim();

            parameters.put(name,
                executionContext.getExpressionExecutor().evaluate(executionContext.getToken(), name));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracted parameters:" + parameters);
        }

        return parameters;
    }

    /**
     * Remove the current user from a collection of User objects.
     *
     * @param users
     */
    private void filterUsers(Collection<String> users) {
        if ((users != null) && filterCurrentUser.booleanValue()) {
            Object currentUser = userAccessor.getCurrentUser();

            for (Iterator<String> iter = users.iterator(); iter.hasNext();) {
                Object user = iter.next();

                if ((user != null) && user.equals(currentUser)) {
                    iter.remove();
                }
            }
        }
    }
}
