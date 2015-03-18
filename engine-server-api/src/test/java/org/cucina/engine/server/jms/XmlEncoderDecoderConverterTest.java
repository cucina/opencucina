package org.cucina.engine.server.jms;

import java.util.ArrayList;
import java.util.Collection;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class XmlEncoderDecoderConverterTest {
    @Mock
    private Session session;
    @Mock
    private TextMessage mess;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testCollection()
        throws Exception {
        XmlEncoderDecoderConverter converter = new XmlEncoderDecoderConverter();
        Collection<Object> coll = new ArrayList<Object>();
        JButton button = new JButton("text");

        coll.add(button);

        JLabel label = new JLabel("text");

        coll.add(label);

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        when(session.createTextMessage(stringCaptor.capture())).thenReturn(mess);

        Message message = converter.toMessage(coll, session);

        verify(session).createTextMessage(stringCaptor.capture());
        System.err.println(stringCaptor.getValue());

        when(mess.getText()).thenReturn(stringCaptor.getValue());

        Object obj = converter.fromMessage(message);

        Collection<?> collect = (Collection<?>) obj;

        assertTrue(collect instanceof ArrayList);

        for (Object o : collect) {
            BeanWrapper bw = new BeanWrapperImpl(o);

            assertEquals("text", bw.getPropertyValue("text"));
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testSingle()
        throws Exception {
        XmlEncoderDecoderConverter converter = new XmlEncoderDecoderConverter();
        JButton button = new JButton("text");

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        when(session.createTextMessage(stringCaptor.capture())).thenReturn(mess);

        Message message = converter.toMessage(button, session);

        verify(session).createTextMessage(stringCaptor.capture());
        System.err.println(stringCaptor.getValue());

        when(mess.getText()).thenReturn(stringCaptor.getValue());

        Object obj = converter.fromMessage(message);

        JButton jb = (JButton) obj;

        assertEquals("text", jb.getText());
    }
}
