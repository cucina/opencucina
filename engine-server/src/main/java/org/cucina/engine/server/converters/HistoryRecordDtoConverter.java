package org.cucina.engine.server.converters;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.server.definition.HistoryRecordDto;
import org.springframework.core.convert.converter.Converter;


/**
 * @author vlevine
 */
public class HistoryRecordDtoConverter
		implements Converter<HistoryRecord, HistoryRecordDto> {
	/**
	 * @param hr .
	 * @return .
	 */
	@Override
	public HistoryRecordDto convert(HistoryRecord hr) {
		HistoryRecordDto dto = new HistoryRecordDto();

		dto.setApprovedBy(hr.getApprovedBy());
		dto.setAssignedTo(hr.getAssignedTo());
		dto.setComments(hr.getComments());
		dto.setModifiedBy(hr.getModifiedBy());
		dto.setModifiedDate(hr.getModifiedDate());
		dto.setReason(hr.getReason());
		dto.setStatus(hr.getStatus());

		return dto;
	}
}
