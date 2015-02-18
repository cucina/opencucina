
package org.cucina.email;

import java.util.Locale;

import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import static org.junit.Assert.assertNotNull;

import org.cucina.email.MimeMessagePreparatorFactory;
import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MimeMessagePreparatorFactoryTest {
    MimeMessagePreparatorFactory factory;

    /**
     *
     * @param emailService The emailService to set.
     */
    public void setEmailService(MimeMessagePreparatorFactory emailService) {
        this.factory = emailService;
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        FreeMarkerConfigurationFactoryBean configurationBean = new FreeMarkerConfigurationFactoryBean();

        configurationBean.setTemplateLoaderPath("classpath:email/");

        factory = new MimeMessagePreparatorFactory();
        factory.setConfiguration(configurationBean.getObject());
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testSunnyDay() {
        MimeMessagePreparator preparator = factory.getInstance("TemplateReject", null, Locale.UK,
                null, null, null, null);

        assertNotNull(preparator);
    }
}