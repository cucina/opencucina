package org.cucina.security.converters;

import org.cucina.security.api.DimensionDto;
import org.cucina.security.api.PermissionDto;
import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author vlevine
 */
public class PermissionDtoConverter
		implements Converter<Permission, PermissionDto> {
	/**
	 * @param source .
	 * @return .
	 */
	@Override
	public PermissionDto convert(Permission source) {
		PermissionDto dto = new PermissionDto();
		Collection<Dimension> dimensions = source.getDimensions();
		Collection<DimensionDto> ddos = new ArrayList<DimensionDto>();

		for (Dimension dimension : dimensions) {
			DimensionDto dd = new DimensionDto();

			dd.setDomainObjectId(dimension.getDomainObjectId().longValue());
			dd.setPropertyName(dimension.getPropertyName());
			ddos.add(dd);
		}

		dto.setDimensions(ddos);
		dto.setName(source.getName());

		return dto;
	}
}
