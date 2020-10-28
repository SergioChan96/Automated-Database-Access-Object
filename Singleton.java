import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sergio on 07.12.2016.
 */
public final class Singleton {

    private static Connection connection =null;

    private static AtomicInteger counter = new AtomicInteger();

    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    private Singleton() {
    }
    public Connection openDatabase(){
        System.out.println(counter.get());
        if (counter.incrementAndGet()==1){
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:resource:TestDB.db";
                connection = DriverManager.getConnection(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public void closeDatabase(){
        System.out.println(counter.get());
        if (counter.decrementAndGet()==0){
            try{
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void closeApplication(){
        System.out.println(counter.get());
        try {
            if(counter.get()>0) {
                connection.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
