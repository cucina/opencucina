package org.cucina.i18n.converter;

import org.springframework.core.convert.converter.Converter;

import org.cucina.core.InstanceFactory;

import org.cucina.i18n.api.ListNodeDto;
import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.service.I18nService;

import reactor.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class ListNodeToDtoConverter
    implements Converter<ListNode, ListNodeDto> {
    private I18nService i18nService;
    private InstanceFactory instanceFactory;

    /**
     * Creates a new ListNodeToDtoConverter object.
     *
     * @param instanceFactory JAVADOC.
     */
    public ListNodeToDtoConverter(InstanceFactory instanceFactory, I18nService i18nService) {
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
        Assert.notNull(i18nService, "i18nService is null");
        this.i18nService = i18nService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param source JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public ListNodeDto convert(ListNode source) {
        ListNodeDto dto = instanceFactory.getBean(ListNodeDto.class.getSimpleName());

        dto.setDefaultValue(source.getDefaultValue());
        dto.setType(source.getType());
        dto.setApplication(source.getLabel().getBaseName());
        dto.setCode(source.getLabel().getMessageCd());
        dto.setText(source.getLabel().getBestMessage(i18nService.getLocale()));
        dto.setLocale(i18nService.getLocale());

        return dto;
    }
}
