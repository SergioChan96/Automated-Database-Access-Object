package Project.TestObjects;

import org.junit.Before;
import org.junit.Test;
import tools.ModelCallback;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class JUnitTest {

    private TestObject t;
    private TestObjectDAO dao;

    @Before
    public void preparation() {
        t = new TestObject("Hey", 5, 2.5);
        dao = new TestObjectDAO();
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
        } catch(ClassNotFoundException x) {
            x.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void testInsertinDatabase(){
        dao.insert(t, new ModelCallback() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                fail();
            }
        });
    }
    @Test
    public void testSelect(){
        dao.select(5, "integer", new ModelCallback<List<TestObject>>() {
            @Override
            public void onComplete(List<TestObject> o) {
                assertEquals(o,t);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                fail();
            }
        });
    }
    @Test
    public void testDeleteinDatabase(){
        dao.delete("string", "string", new ModelCallback() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                fail();
            }
        });
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
