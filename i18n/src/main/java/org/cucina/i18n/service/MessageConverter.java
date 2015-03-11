package org.cucina.i18n.service;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.LocaleUtils;

import org.springframework.core.convert.converter.Converter;

import org.cucina.i18n.MessageDto;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.model.MutableI18nMessage;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MessageConverter
    implements Converter<Message, Collection<MessageDto>> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<MessageDto> convert(Message message) {
        Collection<MessageDto> result = new LinkedList<MessageDto>();
        Collection<MutableI18nMessage> i18nMessages = message.getInternationalisedMessages();

        for (MutableI18nMessage mutableI18nMessage : i18nMessages) {
            MessageDto mdto = new MessageDto();

            mdto.setId(message.getId());
            mdto.setApplication(message.getBaseName());
            mdto.setCode(message.getMessageCd());
            mdto.setLocale(LocaleUtils.toLocale(mutableI18nMessage.getLocaleCd()));
            mdto.setText(mutableI18nMessage.getMessageTx());
            result.add(mdto);
        }

        return result;
    }
}
