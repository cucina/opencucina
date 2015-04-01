package org.cucina.i18n.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.api.ListItemService;
import org.cucina.i18n.model.ListItem;
import org.cucina.i18n.repository.ListItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@Service
public class ListItemServiceImpl
    implements ListItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ListItemServiceImpl.class);
    private ConversionService conversionService;
    private ListItemRepository listNodeRepository;

    /**
     * Creates a new ListNodeServiceImpl object.
     *
     * @param listNodeRepository JAVADOC.
     * @param conversionService JAVADOC.
     */
    @Autowired
    public ListItemServiceImpl(ListItemRepository listNodeRepository,
        @Qualifier(value = "myConversionService")
    ConversionService conversionService) {
        Assert.notNull(listNodeRepository, "listNodeRepository is null");
        this.listNodeRepository = listNodeRepository;
        Assert.notNull(conversionService, "conversionService is null");
        this.conversionService = conversionService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public ListItemDto load(Long id) {
        ListItem ln = listNodeRepository.findById(id);

        return conversionService.convert(ln, ListItemDto.class);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<ListItemDto> loadByType(String type) {
        Collection<ListItem> lns = listNodeRepository.findByType(type);
        Collection<ListItemDto> result = new ArrayList<ListItemDto>();

        for (ListItem listNode : lns) {
            result.add(conversionService.convert(listNode, ListItemDto.class));
        }

        return result;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNodeDto JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional
    public Long save(ListItemDto listNodeDto) {
        ListItem node = conversionService.convert(listNodeDto, ListItem.class);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving " + node);
        }

        return listNodeRepository.save(node).getId();
    }
}
