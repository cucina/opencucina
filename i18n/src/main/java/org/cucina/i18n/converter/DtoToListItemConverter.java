package org.cucina.i18n.converter;

import org.springframework.core.convert.converter.Converter;

import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.model.ListItem;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;

import reactor.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class DtoToListItemConverter
    implements Converter<ListItemDto, ListItem> {
    private MessageRepository messageRepository;

    /**
     * Creates a new DtoToListNodeConverter object.
     *
     * @param instanceFactory JAVADOC.
     */
    public DtoToListItemConverter(MessageRepository messageRepository) {
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
    public ListItem convert(ListItemDto source) {
        ListItem node = new ListItem();

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
