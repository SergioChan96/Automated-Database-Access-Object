package TestObjects;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;


public class JUnitTest {

    private TestObject t;

    @Before
    public void preparation() {
        t = new TestObject("Hey", 5, 2.5);
    }

    @Test
    public void getNamesOfFieldsinClass() {

        System.out.println(t.getClass().getName());

        try {
        Class<?> c = Class.forName(t.getClass().getName());
        System.out.println(t.getClass().getDeclaredFields()[1]);
        Field[] field = t.getClass().getDeclaredFields();
        field[0].setAccessible(true);
        if (field[0].getGenericType().equals(Integer.TYPE)){
            field[0].setInt(t,1);
            System.out.println(t.getInteger());
        }else if(field[0].getGenericType().equals(Double.TYPE)){
            field[0].setDouble(t,5.5);
            System.out.println(t.getDouble());
        }else if(field[0].getGenericType().equals(String.class)) {
            field[0].set(t, "string");
            System.out.println(t.getString());
        }
        //System.out.format("Type: %s%n", field[1].getType());
        //System.out.format("GenericType: %s%n", field[0].getGenericType());
        //System.out.println(runGetter(field[0], t));

        } catch(ClassNotFoundException x) {
            x.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void testInsertinDatabase(){

    }
    /*
    public void getGetterNames() {

        // MZ: Find the correct method
        for (Method method : t.getClass().getMethods()) {
            System.out.println(method.getName());
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                    } catch (IllegalAccessException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }

                }
            }
        }
    }
    public static Object runSetter(Field field) {
        // MZ: Find the correct method
        for (Method method : o.getClass().getMethods()) {
            if ((method.getName().startsWith("set")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                        return method.invoke(o);
                    }
                    catch (IllegalAccessException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }
                }
            }
        }
        return null;

    }

    public static Object runGetter(Field field, Object o) {
        // MZ: Find the correct method
        for (Method method : o.getClass().getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                try {
                    return method.invoke(o);
                }
                catch (IllegalAccessException e) {
                    System.out.println("Could not determine method: " + method.getName());
                }
                catch (InvocationTargetException e) {
                    System.out.println("Could not determine method: " + method.getName());
                }

                }
            }
        }
        return null;
    }
    public static Object runSetter(Field field) {
        // MZ: Find the correct method
        for (Method method : o.getClass().getMethods()) {
            if ((method.getName().startsWith("set")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                        return method.invoke(o);
                    }
                    catch (IllegalAccessException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }
                    catch (InvocationTargetException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }

                }
            }
        }
        return null;
    }
     */
}
