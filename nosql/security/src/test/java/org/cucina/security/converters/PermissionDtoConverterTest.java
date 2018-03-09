package org.cucina.security.converters;

import org.cucina.security.api.DimensionDto;
import org.cucina.security.api.PermissionDto;
import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @author vlevine
 */
public class PermissionDtoConverterTest {
	private PermissionDtoConverter converter;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		converter = new PermissionDtoConverter();
	}

	/**
	 *
	 */
	@Test
	public void testConvert() {
		Permission p = new Permission();

		p.setName("name");

		Collection<Dimension> dimensions = new ArrayList<Dimension>();
		Dimension dd = new Dimension();

		dd.setDomainObjectId(BigInteger.valueOf(100L));
		dd.setPropertyName("propertyName");
		dimensions.add(dd);
		p.setDimensions(dimensions);

		PermissionDto bean = converter.convert(p);

		assertEquals("name", bean.getName());

		Collection<DimensionDto> dims = bean.getDimensions();

		assertNotNull(dims);

		DimensionDto d = dims.iterator().next();

		assertEquals(100L, d.getDomainObjectId().longValue());
		assertEquals("propertyName", d.getPropertyName());
	}
}
