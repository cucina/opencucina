package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UsersEditor
    extends PropertyEditorSupport {
    private static final Logger LOG = LoggerFactory.getLogger(UsersEditor.class);
    private UserRepository userRepository;

    /**
     * Creates a new UsersEditor object.
     *
     * @param userDao JAVADOC.
     */
    public UsersEditor(UserRepository userRepository) {
        Assert.notNull(userRepository);
        this.userRepository = userRepository;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param text JAVADOC.
    *
    * @throws IllegalArgumentException JAVADOC.
    */
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        Collection<User> users = new HashSet<User>();

        if (StringUtils.isNotEmpty(text)) {
            String[] userNames = text.split(",");

            for (int i = 0; i < userNames.length; i++) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Attemting to retrieve User by username: " + userNames[i]);
                }

                User user = userRepository.findByUsername(userNames[i].trim());

                if (user == null) {
                    throw new OptimisticLockingFailureException("The user [" + userNames[i] +
                        "] is not available");
                }

                users.add(user);
            }
        }

        setValue(users);
    }
}
