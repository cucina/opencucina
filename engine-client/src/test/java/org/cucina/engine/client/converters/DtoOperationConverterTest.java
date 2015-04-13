package org.cucina.engine.client.converters;

import java.util.HashMap;

import org.springframework.expression.BeanResolver;

import org.cucina.engine.client.Operation;
import org.cucina.engine.server.definition.WorkflowElementDto;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class DtoOperationConverterTest {
    @Mock
    private BeanResolver beanResolver;
    private DtoOperationConverter converter;
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    @Mock
    private Operation op;
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
        converter = new DtoOperationConverter(beanResolver);
        when(dto.getPath()).thenReturn("opName");
        when(beanResolver.resolve(null, "opName")).thenReturn(op);
        when(dto.getProperties()).thenReturn(properties);
    }

    /**
     *
     */
    @Test
    public void testConvert() {
        Operation result = converter.convert(dto);

        assertEquals(op, result);
    }
}
