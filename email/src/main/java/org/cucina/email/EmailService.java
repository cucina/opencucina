
package org.cucina.email;

import java.util.Collection;
import java.util.Map;

import javax.activation.DataSource;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface EmailService {
    /**
    * JAVADOC.
    *
    * @param descriptor JAVADOC.
    *
    * @return JAVADOC.
    */
    public void sendMessages(String messageKey, Collection<EmailUser> toUsers,
        Collection<EmailUser> ccUsers, Collection<EmailUser> bccUsers,
        Map<Object, Object> parameters, Collection<DataSource> attachments);
}