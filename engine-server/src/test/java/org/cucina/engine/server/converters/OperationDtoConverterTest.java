package org.cucina.engine.server.converters;

import org.cucina.engine.server.definition.OperationDescriptor;
import org.cucina.engine.server.definition.WorkflowElementDto;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


/**
 *
 *
 * @author vlevine
  */
public class OperationDtoConverterTest {
    private OperationDtoConverter converter;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        converter = new OperationDtoConverter();
    }

    /**
     *
     */
    @Test
    public void testConvert() {
        OperationDescriptor source = new OperationDescriptor();

        source.put("application", "APP");
        source.put("domainId", "DI");
        source.put("domainType", "DT");
        source.put("path", "PA");

        WorkflowElementDto dto = converter.convert(source);

        assertEquals("APP", dto.getApplication());
        assertEquals("DI", dto.getDomainId());
        assertEquals("DT", dto.getDomainType());
        assertEquals("PA", dto.getPath());
    }
}
