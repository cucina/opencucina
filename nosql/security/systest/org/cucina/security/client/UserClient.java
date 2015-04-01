package org.cucina.security.client;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.cucina.security.api.DimensionDto;
import org.cucina.security.api.PermissionDto;
import org.cucina.security.api.UserDto;
import org.junit.Test;


/**
 *
 *
 * @author vlevine
  */
public class UserClient {
    private static final String ACCESS_URL = "http://localhost:8080/user";

    /**
     *
     */
    @SuppressWarnings("rawtypes")
	@Test
    public void testCrud()
        throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        UserDto userDto = new UserDto();

        userDto.setEmail("email@email.com");
        userDto.setPassword("password");
        userDto.setUsername("username");

        ResponseEntity<Long> rid = restTemplate.postForEntity(ACCESS_URL, userDto, Long.class);

        System.err.println(rid);

        ResponseEntity<UserDto> rdto = restTemplate.getForEntity(ACCESS_URL + "/{username}",
                UserDto.class, "username");

        UserDto dto = rdto.getBody();
        Collection<PermissionDto> permissions = new ArrayList<PermissionDto>();
        PermissionDto pd = new PermissionDto();

        pd.setName("perm1");

        Collection<DimensionDto> dimensions = new ArrayList<DimensionDto>();
        DimensionDto dd = new DimensionDto();

        dd.setDomainObjectId(20L);
        dd.setPropertyName("propertyName");
        dimensions.add(dd);
        pd.setDimensions(dimensions);
        permissions.add(pd);
        dto.setPermissions(permissions);
        dto.setId(rid.getBody());
        dto.setPassword("passwordX");
        System.err.println(dto);

        rid = restTemplate.postForEntity(ACCESS_URL, dto, Long.class);
        System.err.println(rid);
        rdto = restTemplate.getForEntity(ACCESS_URL + "/{username}", UserDto.class, "username");

        dto = rdto.getBody();

        ResponseEntity<Collection> cr = restTemplate.getForEntity(ACCESS_URL, Collection.class);
        Collection coll = cr.getBody();

        System.err.println(coll);

        ResponseEntity<Boolean> rb = restTemplate.getForEntity(ACCESS_URL + "/delete/{username}",
                Boolean.class, "username");

        System.err.println(rb.getBody());
    }
}
