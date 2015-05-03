package org.cucina.engine.server.converters;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.cucina.engine.server.definition.CheckDescriptor;
import org.cucina.engine.server.definition.ProcessElementDto;


/**
 *
 *
 * @author vlevine
  */
public class CheckDtoConverterTest {
    private CheckDtoConverter converter;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        converter = new CheckDtoConverter();
    }

    /**
     *
     */
    @Test
    public void testConvert() {
        CheckDescriptor source = new CheckDescriptor();

        source.put("application", "APP");
        source.put("domainId", "DI");
        source.put("domainType", "DT");
        source.put("path", "PA");

        ProcessElementDto dto = converter.convert(source);

        assertEquals("APP", dto.getApplication());
        assertEquals("DI", dto.getDomainId());
        assertEquals("DT", dto.getDomainType());
        assertEquals("PA", dto.getPath());
    }
}
