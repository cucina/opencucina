package org.cucina.security.config;

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
        registerBeanDefinitionParser(PreferenceRepositoryDefinitionParser.DEFAULT_ID,
            new PreferenceRepositoryDefinitionParser());
        registerBeanDefinitionParser(PrivilegeRepositoryDefinitionParser.DEFAULT_ID,
            new PrivilegeRepositoryDefinitionParser());
        registerBeanDefinitionParser(RoleRepositoryDefinitionParser.DEFAULT_ID,
            new RoleRepositoryDefinitionParser());
        registerBeanDefinitionParser(UserRepositoryDefinitionParser.DEFAULT_ID,
            new UserRepositoryDefinitionParser());
        registerBeanDefinitionParser(PermissionRepositoryDefinitionParser.DEFAULT_ID,
            new PermissionRepositoryDefinitionParser());
    }
}
