package org.cucina.email.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class NamespaceHandler
    extends NamespaceHandlerSupport {
    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void init() {
        registerBeanDefinitionParser("emailService", new EmailDefinitionParser());
    }
}
