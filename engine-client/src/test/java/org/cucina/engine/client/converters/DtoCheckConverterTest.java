package org.cucina.engine.client.converters;

import java.util.HashMap;

import org.springframework.expression.BeanResolver;

import org.cucina.engine.client.Check;
import org.cucina.engine.server.definition.WorkflowElementDto;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class DtoCheckConverterTest {
    @Mock
    private BeanResolver beanResolver;
    @Mock
    private Check check;
    private DtoCheckConverter converter;
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    @Mock
    private WorkflowElementDto dto;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        converter = new DtoCheckConverter(beanResolver);
        when(dto.getPath()).thenReturn("checkName");
        when(beanResolver.resolve(null, "checkName")).thenReturn(check);
        when(dto.getProperties()).thenReturn(properties);
    }

    /**
     *
     */
    @Test
    public void testConvert() {
        Check result = converter.convert(dto);

        assertEquals(check, result);
    }
}
