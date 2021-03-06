package org.cucina.engine.server.converters;

import org.cucina.engine.server.definition.OperationDescriptor;
import org.cucina.engine.server.definition.OperationDescriptorDto;
import org.cucina.engine.server.definition.ProcessElementDto;
import org.springframework.core.convert.converter.Converter;


/**
 * @author vlevine
 */
public class OperationDtoConverter
		implements Converter<OperationDescriptor, ProcessElementDto> {
	/**
	 * @param source .
	 * @return .
	 */
	@Override
	public ProcessElementDto convert(OperationDescriptor source) {
		OperationDescriptorDto dto = new OperationDescriptorDto();

		dto.setApplication((String) source.get("application"));
		dto.setDomainId((source.get("domainId") == null) ? null : source.get("domainId").toString());
		dto.setDomainType((String) source.get("domainType"));
		dto.setPath((String) source.get("path"));
		dto.setProperties(source.getProperties());

		return dto;
	}
}
