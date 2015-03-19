package org.cucina.i18n.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.cucina.i18n.api.ListNodeDto;
import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.repository.ListNodeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@Service
public class ListNodeServiceImpl
    implements ListNodeService {
    private static final Logger LOG = LoggerFactory.getLogger(ListNodeServiceImpl.class);
    private ConversionService conversionService;
    private ListNodeRepository listNodeRepository;

    /**
     * Creates a new ListNodeServiceImpl object.
     *
     * @param listNodeRepository JAVADOC.
     * @param conversionService JAVADOC.
     */
    @Autowired
    public ListNodeServiceImpl(ListNodeRepository listNodeRepository,
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
    public ListNodeDto load(Long id) {
        ListNode ln = listNodeRepository.find(id);

        return conversionService.convert(ln, ListNodeDto.class);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<ListNodeDto> loadByType(String type) {
        Collection<ListNode> lns = listNodeRepository.findByType(type);
        Collection<ListNodeDto> result = new ArrayList<ListNodeDto>();

        for (ListNode listNode : lns) {
            result.add(conversionService.convert(listNode, ListNodeDto.class));
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
    public Long save(ListNodeDto listNodeDto) {
        ListNode node = conversionService.convert(listNodeDto, ListNode.class);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving " + node);
        }

        return listNodeRepository.save(node);
    }
}
