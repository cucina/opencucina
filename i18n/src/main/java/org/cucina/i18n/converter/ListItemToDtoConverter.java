package org.cucina.i18n.converter;

import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.model.ListItem;
import org.cucina.i18n.service.I18nService;
import org.springframework.core.convert.converter.Converter;
import reactor.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
public class ListItemToDtoConverter
		implements Converter<ListItem, ListItemDto> {
	private I18nService i18nService;

	/**
	 * Creates a new ListNodeToDtoConverter object.
	 *
	 * @param instanceFactory JAVADOC.
	 */
	public ListItemToDtoConverter(I18nService i18nService) {
		Assert.notNull(i18nService, "i18nService is null");
		this.i18nService = i18nService;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param source JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public ListItemDto convert(ListItem source) {
		ListItemDto dto = new ListItemDto();

		dto.setDefaultValue(source.getDefaultValue());
		dto.setType(source.getType());
		dto.setApplication(source.getLabel().getBaseName());
		dto.setCode(source.getLabel().getMessageCd());
		dto.setText(source.getLabel().getBestMessage(i18nService.getLocale()));
		dto.setLocale(i18nService.getLocale());

		return dto;
	}
}
