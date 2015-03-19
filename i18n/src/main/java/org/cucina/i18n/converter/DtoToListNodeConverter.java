package org.cucina.i18n.converter;

import org.springframework.core.convert.converter.Converter;

import org.cucina.core.InstanceFactory;

import org.cucina.i18n.api.ListNodeDto;
import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;

import reactor.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class DtoToListNodeConverter
    implements Converter<ListNodeDto, ListNode> {
    private InstanceFactory instanceFactory;
    private MessageRepository messageRepository;

    /**
     * Creates a new DtoToListNodeConverter object.
     *
     * @param instanceFactory JAVADOC.
     */
    public DtoToListNodeConverter(InstanceFactory instanceFactory,
        MessageRepository messageRepository) {
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
        Assert.notNull(messageRepository, "messageRepository is null");
        this.messageRepository = messageRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param source JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public ListNode convert(ListNodeDto source) {
        ListNode node = instanceFactory.getBean(ListNode.class.getSimpleName());

        node.setDefaultValue(source.getDefaultValue());
        node.setType(source.getType());

        String basename = source.getApplication();
        String code = source.getCode();
        Message label = messageRepository.save(basename, source.getLocale().toString(), code,
                source.getText());

        node.setLabel(label);

        return node;
    }
}
