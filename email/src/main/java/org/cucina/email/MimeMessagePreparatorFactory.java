
package org.cucina.email;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.collections.MapUtils;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.MimeMessagePreparator;

import freemarker.template.Configuration;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MimeMessagePreparatorFactory {
    private Configuration configuration;

    // Injected parameter map containing standard info for the email 
    private Map<String, String> standardParams;
    private String from;
    private String suffix;

    /**
     * @param configuration
     *            The configuration to set.
     */
    @Required
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * @param from
     *            The from to set.
     */
    @Required
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param templateName JAVADOC.
     * @param params JAVADOC.
     * @param locale JAVADOC.
     * @param tos JAVADOC.
     * @param ccs JAVADOC.
     * @param bccs JAVADOC.
     *
     * @return JAVADOC.
     */
    public MimeMessagePreparator getInstance(String templateName, Map<Object, Object> params,
        Locale locale, Collection<?extends EmailUser> tos, Collection<?extends EmailUser> ccs,
        Collection<?extends EmailUser> bccs, Collection<DataSource> attachments) {
        if (params == null) {
            params = new HashMap<Object, Object>();
        }

        if (MapUtils.isNotEmpty(getStandardParams())) {
            params.putAll(getStandardParams());
        }

        MimeMessagePreparatorImpl preparator = new MimeMessagePreparatorImpl();

        preparator.setTemplateName(templateName);
        preparator.setConfiguration(configuration);
        preparator.setFrom(from);
        preparator.setParams(params);
        preparator.setLocale(locale);
        preparator.setSuffix(suffix);
        preparator.setTo(tos);
        preparator.setCc(ccs);
        preparator.setBcc(bccs);
        preparator.setAttachments(attachments);

        return preparator;
    }

    /**
     * JAVADOC.
     *
     * @param standardParams
     *            JAVADOC.
     */
    public void setStandardParams(Map<String, String> standardParams) {
        this.standardParams = standardParams;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public Map<String, String> getStandardParams() {
        return standardParams;
    }

    /**
     * Set suffix
     *
     * @param suffix String
     */
    @Required
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
