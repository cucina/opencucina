package org.cucina.security.converters;

import java.math.BigInteger;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.core.convert.converter.Converter;

import org.cucina.security.api.DimensionDto;
import org.cucina.security.api.PermissionDto;
import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;


/**
 * 
 *
 * @author vlevine
  */
public class DtoPermissionConverter
    implements Converter<PermissionDto, Permission> {
    /**
     *
     *
     * @param dto .
     *
     * @return .
     */
    @Override
    public Permission convert(PermissionDto dto) {
        Permission p = new Permission();

        p.setName(dto.getName());

        Collection<Dimension> dimensions = new HashSet<Dimension>();

        for (DimensionDto dd : dto.getDimensions()) {
            Dimension d = new Dimension();

            d.setDomainObjectId(BigInteger.valueOf(dd.getDomainObjectId()));
            d.setPropertyName(dd.getPropertyName());
            dimensions.add(d);
        }

        p.setDimensions(dimensions);

        return p;
    }
}
