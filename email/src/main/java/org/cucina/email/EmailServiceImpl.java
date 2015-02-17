
package org.cucina.email;

import java.util.Collection;
import java.util.Map;

import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service which sends emails to the intended recipients.
 *
 * @author $author$
 * @version $Revision: 1.16 $
 * @param <T>
 * @param <T>
 */
public class EmailServiceImpl
    implements EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);
    private EmailConstructor emailConstructor;
    private JavaMailSender javaMailSender;

    /**
     * Set emailConstructor
     *
     * @param emailConstructor ConstructEmailService.
     */
    public void setEmailConstructor(EmailConstructor emailConstructor) {
        this.emailConstructor = emailConstructor;
    }

    /**
     * Set javaMailSender
     *
     * @param javaMailSender The javaMailSender to set.
     */
    @Required
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageKey JAVADOC.
     * @param toUsers JAVADOC.
     * @param ccUsers JAVADOC.
     * @param bccUsers JAVADOC.
     * @param parameters JAVADOC.
     * @param attachments JAVADOC.
     */
    @Override
    public void sendMessages(String messageKey, Collection<EmailUser> toUsers,
        Collection<EmailUser> ccUsers, Collection<EmailUser> bccUsers,
        Map<Object, Object> parameters, Collection<DataSource> attachments) {
        MimeMessagePreparator[] preparators = emailConstructor.prepareMessages(messageKey, toUsers,
                ccUsers, bccUsers, parameters, attachments);

        if (LOG.isDebugEnabled()) {
            for (int i = 0; i < preparators.length; i++) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("MessagePreparator=" + preparators[i]);
                }
            }
        }

        try {
            javaMailSender.send(preparators);
        } catch (MailSendException e) {
            // The Spring class SmartMimeMessage is not serializable, which
            // prevents Mule from being able to insert into the error queue.
            // Hence we need to handle this exception, which contains a
            // reference to that class.
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < preparators.length; i++) {
                sb.append("MessagePreparator=").append(preparators[i]).append("  ");
            }

            LOG.error("Caught exception when sending email:" + sb, e);
            throw e;
        }
    }
}
