package org.cucina.core.marshal;

import java.util.Collection;
import java.util.HashSet;

import org.cucina.core.testassist.Bar;
import org.cucina.core.testassist.Foo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


/**
 * Test that JacksonMarshaller functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JacksonMarshallerTest {
    private static final String MARSHALLED_FOO = "{\"id\":null,\"bars\":null,\"bazs\":null,\"date\":null,\"message\":null,\"name\":\"Mikes\",\"value\":0,\"version\":0}";
    private JacksonMarshaller marshaller;

    /**
     * Requires graph parameter to be set
     */
    @Test(expected = IllegalArgumentException.class)
    public void marshallRequiresGraph() {
        marshaller.marshall(null);
    }

    /**
     * Test barfs if exception
     */
    @Test(expected = RuntimeException.class)
    public void marshallingException() {
        Collection<Marshaller> marshallers = new HashSet<Marshaller>();

        marshallers.add(new JacksonMarshaller(null, null));

        marshaller.marshall(marshallers);
    }

    /**
     * Test marshalls
     *
     * @throws Exception.
     */
    @Test
    public void marshalls()
        throws Exception {
        Foo foo = new Foo();

        foo.setName("Mikes");

        String result = marshaller.marshall(foo);

        assertNotNull("Should have converted to String", result);
        assertEquals("Should have converted to String", MARSHALLED_FOO, result);
    }

    /**
     * Set up test
     */
    @Before
    public void setup()
        throws Exception {
        marshaller = new JacksonMarshaller(null, null);
        marshaller.afterPropertiesSet();
    }

    /**
     * Test unmarshalls foo correctly
     */
    @Test
    public void unmarshall()
        throws Exception {
        marshaller = new JacksonMarshaller(null, null);
        marshaller.afterPropertiesSet();

        Foo foo = marshaller.unmarshall(MARSHALLED_FOO, Foo.class);

        assertNotNull("Should have unmarshalled Projection", foo);
        assertEquals("Incorrect name", "Mikes", foo.getName());
    }

    /**
     * Test unmarshall with Exception
     *
     * @throws Exception.
     */
    @Test(expected = RuntimeException.class)
    public void unmarshallException()
        throws Exception {
        marshaller.unmarshall(MARSHALLED_FOO, Bar.class);
    }

    /**
     * Unmarshall requires targetClass parameter
     */
    @Test(expected = IllegalArgumentException.class)
    public void unmarshallRequiresClass() {
        marshaller.unmarshall(MARSHALLED_FOO, (Class<?>) null);
    }

    /**
     * Unmarshall requires source parameter to be set
     */
    @Test(expected = IllegalArgumentException.class)
    public void unmarshallRequiresSource() {
        marshaller.unmarshall(null, Foo.class);
    }
}
