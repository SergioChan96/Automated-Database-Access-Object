package Project.TestObjects;

import org.junit.Before;
import org.junit.Test;
import tools.ModelCallback;
import tools.Singleton;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.*;


public class JUnitTest {

    private TestObject t;
    private TestObjectDAO dao;

    @Before
    public void preparation() {
        t = new TestObject("Hey", 5, 2.5);
        dao = new TestObjectDAO();

        Connection conn = Singleton.getInstance().openDatabase();
        try {
            String sql = "SELECT name FROM sqlite_master WHERE type='table'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet resultset = ps.executeQuery();
            if(resultset.next()) {
                System.out.println(resultset.getString("name"));
            }else{
                sql = "CREATE TABLE test (string VARCHAR, integer INTEGER, double NUMERIC )";
                ps = conn.prepareStatement(sql);
                ps.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }finally {
            try {
                Singleton.getInstance().closeDatabase();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }
/*
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
    */
    @Test
    public void checkDAO(){
        System.out.println(t.getString());
        dao.insert(t, new ModelCallback() {
            @Override
            public void onComplete(Object o) {
            }
            @Override
            public void onError(Exception e) {
                fail();
            }
        });
        dao.select(5, "integer", new ModelCallback<List<TestObject>>() {
            @Override
            public void onComplete(List<TestObject> o) {
                assertEquals(o.get(0),t);
            }
            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
        dao.entryExistAlready("hey", "string", new ModelCallback<Boolean>() {
            @Override
            public void onComplete(Boolean aBoolean) {
                assertTrue(aBoolean);
            }
            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
        dao.getAllData(new ModelCallback<List<TestObject>>() {
            @Override
            public void onComplete(List<TestObject> testObjects) {
                for (TestObject o : testObjects){
                    System.out.println(o.getString() + " "+o.getDouble()+" "+o.getInteger());
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
        dao.delete("string", "string", new ModelCallback() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
    }
    public void testDeleteinDatabase(){
        dao.delete("string", "string", new ModelCallback() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
    }
    // TODO Test comparison mistake numeric = integer or better type finding/comparison

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
