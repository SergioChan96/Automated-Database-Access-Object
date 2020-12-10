package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.atomic.AtomicInteger;

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
        if (counter.incrementAndGet()==1){
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:Resources/TestDB.db";
                connection = DriverManager.getConnection(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public void closeDatabase(){
        if (counter.decrementAndGet()==0){
            try{
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void closeApplication(){
        try {
            if(counter.get()>0) {
                connection.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
