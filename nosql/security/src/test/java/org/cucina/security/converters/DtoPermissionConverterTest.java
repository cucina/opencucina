package org.cucina.security.converters;

import org.cucina.security.api.DimensionDto;
import org.cucina.security.api.PermissionDto;
import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @author vlevine
 */
public class DtoPermissionConverterTest {
	private DtoPermissionConverter converter;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		converter = new DtoPermissionConverter();
	}

	/**
	 *
	 */
	@Test
	public void testConvert() {
		PermissionDto dto = new PermissionDto();

		dto.setName("name");

		Collection<DimensionDto> dimensions = new ArrayList<DimensionDto>();
		DimensionDto dd = new DimensionDto();

		dd.setDomainObjectId(100L);
		dd.setPropertyName("propertyName");
		dimensions.add(dd);
		dto.setDimensions(dimensions);

		Permission bean = converter.convert(dto);

		assertEquals("name", bean.getName());

		Collection<Dimension> dims = bean.getDimensions();

		assertNotNull(dims);

		Dimension d = dims.iterator().next();

		assertEquals(100L, d.getDomainObjectId().longValue());
		assertEquals("propertyName", d.getPropertyName());
	}
}
