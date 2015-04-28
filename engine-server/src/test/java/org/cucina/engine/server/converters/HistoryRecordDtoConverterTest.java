package org.cucina.engine.server.converters;

import java.util.Date;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.server.communication.HistoryRecordDto;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


/**
 *
 *
 * @author vlevine
  */
public class HistoryRecordDtoConverterTest {
    private HistoryRecordDtoConverter converter;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        converter = new HistoryRecordDtoConverter();
    }

    /**
     *
     */
    @Test
    public void testConvert() {
        HistoryRecord source = new HistoryRecord();

        source.setApprovedBy("ApprovedBy");
        source.setAssignedTo("AssignedTo");
        source.setComments("Comments");
        source.setModifiedBy("ModifiedBy");

        Date date = new Date();

        source.setModifiedDate(date);
        source.setReason("Reason");
        source.setStatus("Status");

        HistoryRecordDto dto = converter.convert(source);

        assertEquals("ApprovedBy", dto.getApprovedBy());
        assertEquals("AssignedTo", dto.getAssignedTo());
        assertEquals("Comments", dto.getComments());
        assertEquals("ModifiedBy", dto.getModifiedBy());
        assertEquals(date, dto.getModifiedDate());
        assertEquals("Reason", dto.getReason());
        assertEquals("Status", dto.getStatus());
    }
}
