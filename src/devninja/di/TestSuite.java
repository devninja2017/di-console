package devninja.di;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class TestSuite {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setup() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void printHello() {
        App.main(new String[]{});
        assertEquals("Hello World", outContent.toString());
    }

    @Test
    public void assemblerHasZeroProvider() {
        Assembler assembler = Assembler.newInstance();
        int providerCount = assembler.getProviders().size();
        assertEquals(0, providerCount);
    }

    @Test
    public void assemblerHasProviders() {
        Assembler assembler = Assembler.newInstance();
        assembler.add(Mockito.mock(Provider.class));
        int providerCount = assembler.getProviders().size();
        assertEquals(1, providerCount);
    }

    @Test
    public void providerHasProvidesAnnotatedMethod() {
        Provider provider = new DemoProvider();
        Method[] methods = provider.getClass().getDeclaredMethods();
        Class cl = null;
        for (Method m : methods) {
            if (m.isAnnotationPresent(Provides.class)) {
                try {
                    Object obj = m.invoke(provider);
                    cl = obj.getClass();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        assertEquals(Foo.class, cl);
    }

    @Test
    public void classHasInjectAnnotatedField() {
        Bar bar = new Bar();
        Field[] fields = bar.getClass().getDeclaredFields();
        Class cl = null;

        int fieldCount = 0;
        for (Field f : fields) {
            if (f.isAnnotationPresent(Inject.class)) {
                fieldCount++;
            }
        }
        assertEquals(true, fieldCount > 0);
    }

    private class DemoProvider implements Provider {

        @Provides
        public Foo providesFoo() {
            return new Foo();
        }
    }

    private class Foo {}

    private class Bar {
        @Inject Foo foo;
    }
}
