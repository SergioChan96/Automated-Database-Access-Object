import TestObjects.TestObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class main{
    public static void main(String args[]){

        Object t = new TestObject("Hey", 5, 2.5);
        System.out.println(t.getClass().getName());

        try {
            Class<?> c = Class.forName(t.getClass().getName());
            System.out.println(t.getClass().getDeclaredFields()[1]);
            Field[] field = t.getClass().getDeclaredFields();
            System.out.format("Type: %s%n", field[1].getType());
            System.out.format("GenericType: %s%n", field[0].getGenericType());
            System.out.println(runGetter(field[0],t));
            Singleton.getInstance().openDatabase();
            Singleton.getInstance().closeDatabase();


            // production code should handle these exceptions more gracefully
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        }
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
}
