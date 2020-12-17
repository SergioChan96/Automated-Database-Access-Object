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
                sql = "CREATE TABLE test (string VARCHAR, integer INTEGER, double NUMERIC(5,2));";
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
                assertEquals(o.get(0).getString(),t.getString());
            }
            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
        dao.entryExistAlready("Hey", "string", new ModelCallback<Boolean>() {
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
        dao.delete("Hey", "string", new ModelCallback() {
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

    @Test
    public void deleteDB(){
        Connection conn = Singleton.getInstance().openDatabase();
        try {
            String sql = "DROP TABLE test";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
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
    @Test
    public void allData(){
        dao.getAllData(new ModelCallback<List<TestObject>>() {
            @Override
            public void onComplete(List<TestObject> testObjects) {
                for (TestObject o : testObjects){
                    System.out.println("Object: "+o.getString() + " "+o.getDouble()+" "+o.getInteger());
                }
            }
            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
    }
    @Test
    public void gettingDouble(){
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
                assertEquals(t.getDouble(), o.get(0).getDouble(),0);
            }
            @Override
            public void onError(Exception e) {
                System.err.println(e);
                fail();
            }
        });
    }
}
