package org.cucina.engine.server.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;


/**
 * Utilise JVM default XmlEncoder/XMLDecoder
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class XmlEncoderDecoderConverter
		implements MessageConverter {
	private static final Logger LOG = LoggerFactory.getLogger(XmlEncoderDecoderConverter.class);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param message JAVADOC.
	 * @return JAVADOC.
	 * @throws JMSException               JAVADOC.
	 * @throws MessageConversionException JAVADOC.
	 */
	@Override
	public Object fromMessage(Message message)
			throws JMSException, MessageConversionException {
		if (message instanceof TextMessage) {
			return convertFromTextMessage((TextMessage) message);
		} else if (message instanceof BytesMessage) {
			return convertFromBytesMessage((BytesMessage) message);
		} else {
			return convertFromMessage(message);
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param object  JAVADOC.
	 * @param session JAVADOC.
	 * @return JAVADOC.
	 * @throws JMSException               JAVADOC.
	 * @throws MessageConversionException JAVADOC.
	 */
	@Override
	public Message toMessage(Object object, Session session)
			throws JMSException, MessageConversionException {
		// TODO allow to preset a diff type of JMS message, at least Bytes in
		// addition
		return session.createTextMessage(encodeToString(object));
	}

	private Object convertFromBytesMessage(BytesMessage message)
			throws JMSException {
		byte[] bytes = new byte[(int) message.getBodyLength()];

		message.readBytes(bytes);

		try {
			LOG.trace("Message bytes:" + new String(bytes, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			//
		}

		return decode(bytes);
	}

	private Object convertFromMessage(Message message) {
		throw new IllegalArgumentException("Unsupported message type [" + message.getClass());
	}

	private Object convertFromTextMessage(TextMessage message)
			throws JMSException {
		String text = message.getText();

		LOG.trace("Message text:" + text);

		byte[] bytes;

		try {
			bytes = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("Oops", e);
			throw new IllegalArgumentException(e);
		}

		return decode(bytes);
	}

	private Object decode(byte[] bytes) {
		XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(bytes));

		try {
			return decoder.readObject();
		} finally {
			decoder.close();
		}
	}

	private String encodeToString(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(bos);

		encoder.writeObject(object);
		encoder.flush();
		encoder.close();

		try {
			return new String(bos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("Oops", e);
			throw new IllegalArgumentException(e);
		}
	}
}
