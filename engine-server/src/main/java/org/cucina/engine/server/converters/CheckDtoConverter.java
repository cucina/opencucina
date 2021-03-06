package org.cucina.engine.server.converters;

import org.cucina.engine.server.definition.CheckDescriptor;
import org.cucina.engine.server.definition.CheckDescriptorDto;
import org.cucina.engine.server.definition.ProcessElementDto;
import org.springframework.core.convert.converter.Converter;


/**
 * @author vlevine
 */
public class CheckDtoConverter
		implements Converter<CheckDescriptor, ProcessElementDto> {
	/**
	 * @param source .
	 * @return .
	 */
	@Override
	public ProcessElementDto convert(CheckDescriptor source) {
		CheckDescriptorDto dto = new CheckDescriptorDto();

		dto.setApplication((String) source.get("application"));
		dto.setDomainId((source.get("domainId") == null) ? null : source.get("domainId").toString());
		dto.setDomainType((String) source.get("domainType"));
		dto.setPath((String) source.get("path"));
		dto.setProperties(source.getProperties());

		return dto;
	}
}
