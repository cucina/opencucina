package org.cucina.core.spring;

import org.cucina.core.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SingletonBeanFactoryTest {
	private SingletonBeanFactory factory;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		factory = (SingletonBeanFactory) SingletonBeanFactory.getInstance();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testContainsBean() {
		BeanFactory beanFactory = mock(BeanFactory.class);

		when(beanFactory.containsBean("name")).thenReturn(true);
		factory.setBeanFactory(beanFactory);

		assertTrue(factory.containsBean("name"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetAliases() {
		BeanFactory beanFactory = mock(BeanFactory.class);

		String[] aliases = new String[]{"haha", "hehe"};

		when(beanFactory.getAliases("name")).thenReturn(aliases);
		factory.setBeanFactory(beanFactory);
		assertEquals((Object) aliases, (Object) factory.getAliases("name"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanClassOfT() {
		BeanFactory beanFactory = mock(BeanFactory.class);

		Foo foo = new Foo();

		when(beanFactory.getBean(Foo.class)).thenReturn(foo);
		factory.setBeanFactory(beanFactory);

		assertEquals(Foo.class, factory.getBean(Foo.class).getClass());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanString() {
		BeanFactory beanFactory = mock(BeanFactory.class);
		Foo foo = new Foo();

		when(beanFactory.getBean("foo")).thenReturn(foo);
		factory.setBeanFactory(beanFactory);
		assertEquals(foo, factory.getBean("foo"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanStringClass() {
		BeanFactory beanFactory = mock(BeanFactory.class);
		Foo foo = new Foo();

		when(beanFactory.getBean("foo", Foo.class)).thenReturn(foo);
		factory.setBeanFactory(beanFactory);
		assertEquals(foo, factory.getBean("foo", Foo.class));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanStringObjectArray() {
		BeanFactory beanFactory = mock(BeanFactory.class);
		Foo foo = new Foo();

		when(beanFactory.getBean("foo", (Object[]) null)).thenReturn(foo);
		factory.setBeanFactory(beanFactory);
		assertEquals(foo, factory.getBean("foo", (Object[]) null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */

    /*@Test
    public void testGetType() {
            BeanFactory beanFactory = mock(BeanFactory.class);

            when(beanFactory.getType("foo")).thenReturn((Class<?>) Foo.class);
            replay(beanFactory);
            factory.setBeanFactory(beanFactory);

            assertEquals(Foo.class, factory.getType("foo"));
            verify(beanFactory);
    }*/

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testIsPrototype() {
		BeanFactory beanFactory = mock(BeanFactory.class);

		when(beanFactory.isPrototype("foo")).thenReturn(false);
		factory.setBeanFactory(beanFactory);
		assertFalse(factory.isPrototype("foo"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testIsSingleton() {
		BeanFactory beanFactory = mock(BeanFactory.class);

		when(beanFactory.isSingleton("foo")).thenReturn(true);
		factory.setBeanFactory(beanFactory);
		assertTrue(factory.isSingleton("foo"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testIsTypeMatch() {
		BeanFactory beanFactory = mock(BeanFactory.class);

		when(beanFactory.isTypeMatch("foo", Foo.class)).thenReturn(true);
		factory.setBeanFactory(beanFactory);
		assertTrue(factory.isTypeMatch("foo", Foo.class));
	}
}
