package org.cucina.i18n;

import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.api.ListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@RestController
@RequestMapping(value = "/listNode")
public class ListItemController {
	@Autowired
	private ListItemService listNodeService;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id JAVADOC.
	 * @return JAVADOC.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ListItemDto getById(@PathVariable
									   Long id) {
		Assert.notNull(id, "id is null");

		return listNodeService.load(id);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param type JAVADOC.
	 * @return JAVADOC.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/type/{type}")
	public Collection<ListItemDto> getByType(@PathVariable
													 String type) {
		return listNodeService.loadByType(type);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param listNode JAVADOC.
	 * @return JAVADOC.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Long save(@RequestBody
					 @Valid
							 ListItemDto listNode) {
		return listNodeService.save(listNode);
	}
}
