package org.cucina.engine.server.definition;

import org.cucina.engine.server.jms.XmlEncoderDecoderConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author vlevine
 */
public class SerializingTest {
	@Mock
	private Session session;
	@Mock
	private TextMessage mess;
	private XmlEncoderDecoderConverter converter;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		converter = new XmlEncoderDecoderConverter();
	}

	/**
	 * @throws Exception .
	 */
	@Test
	public void testXmlCheck()
			throws Exception {
		CheckDescriptor desc = new CheckDescriptor();

		desc.put("application", "application");
		desc.setDescription("description");
		desc.put("domainId", new BigInteger(16, new Random()).toString());
		desc.put("domainType", "foo");
		desc.put("path", "path://path");

		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

		when(session.createTextMessage(stringCaptor.capture())).thenReturn(mess);

		Message message = converter.toMessage(desc, session);

		verify(session).createTextMessage(stringCaptor.capture());
		System.err.println(stringCaptor.getValue());

		when(mess.getText()).thenReturn(stringCaptor.getValue());

		Object obj = converter.fromMessage(message);

		CheckDescriptor jdesc = (CheckDescriptor) obj;

		assertEquals("path://path", jdesc.get("path"));
	}
}
