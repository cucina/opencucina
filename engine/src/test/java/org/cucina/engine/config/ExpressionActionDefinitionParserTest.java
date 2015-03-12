
package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.cucina.engine.operations.ExpressionOperation;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ExpressionActionDefinitionParserTest {
    private ExpressionOperationDefinitionParser parser = new ExpressionOperationDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(ExpressionOperation.class, parser.getBeanClass(null));
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
