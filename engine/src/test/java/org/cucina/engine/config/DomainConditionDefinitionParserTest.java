
package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.cucina.engine.checks.DomainExpressionCheck;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DomainConditionDefinitionParserTest {
    private DomainExpressionCheckDefinitionParser parser = new DomainExpressionCheckDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(DomainExpressionCheck.class, parser.getBeanClass(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSetsPrototype() {
        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        parser.doParse(null, builder);
        verify(builder).setScope(BeanDefinition.SCOPE_PROTOTYPE);
    }
}
