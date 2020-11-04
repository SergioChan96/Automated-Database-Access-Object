import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

// IMPORTANT: Always have the data in the same sequence as they appear in the database
public  abstract  class DAO<T> {
    protected  String LOG_TAG = this.getClass().getName();
    protected Connection conn = null;
    protected PreparedStatement ps = null;
    protected ResultSet resultset = null;
    protected ResultSetMetaData rsmd = null;
    protected T t;


    public DAO(T t){
        this.t = t;
    }
/*
    public void insert(String table, Object t,String[] columns, ModelCallback c) {

        //getting the Metadata of the table for insertion
        String sql1 = "SELECT * FROM " + table;
        ps = conn.prepareStatement(sql1);
        resultset = ps.executeQuery();
        rsmd = resultset.getMetaData();
        resultset.next();
        int count = rsmd.getColumnCount();
        for(int i =0; )
        if (rsmd.getColumnName().toLowerCase() == "id")
        for (int i = 0; i < count-2; i++) {
            sql = sql + ",?";
        }

        conn=Singleton.getInstance().openDatabase();
        boolean checkTransaction = false;
        String sql = "INSERT INTO " + table +" (";
        for (int i = 1; i<columns.length; i++){
            if (i==columns.length-1){
                sql=sql+columns[i]+") values(?";
            }else{
                sql=sql+columns[i]+ ",";
            }
        }
        try {
            checkTransaction = conn.getAutoCommit();
            String sql1 = "SELECT * FROM " + table;
            ps = conn.prepareStatement(sql1);
            resultset = ps.executeQuery();
            rsmd = resultset.getMetaData();
            resultset.next();
            int count = rsmd.getColumnCount();
            for (int i = 0; i < count-2; i++) {
                sql = sql + ",?";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            c.onError(e);
        }
        sql = sql + ")";
        print(sql);
        Gson gson = new Gson();
        String json = gson.toJson(t);
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        print(jsonObject.toString());

        try {
            if (checkTransaction) {
                conn.setAutoCommit(false);
            }
            ps = this.conn.prepareStatement(sql);
            fromJson(ps,rsmd,jsonObject);
            ps.executeUpdate();
            if (checkTransaction){
                conn.commit();
            }
            c.onComplete(new Object());
        }catch(SQLException e) {
            e.printStackTrace();
            c.onError(e);
            if (conn != null) {
                try {
                    errPrint("Transaction is being rolled back");
                    if (checkTransaction) {
                        conn.rollback();
                    }
                } catch (SQLException excep) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            c.onError(e);
        } finally {
            try {
                if(checkTransaction) {
                    conn.setAutoCommit(true);
                }
                closeConnection();
            }catch (Exception e){
                e.printStackTrace();
                c.onError(e);
            }
        }
    }


    public void select(Object object, String table, String whereclause, ModelCallback<List<T>> c) {
        conn=Singleton.getInstance().openDatabase();
        List<T> tlist = new ArrayList<T>();
        try {
            String sql = "SELECT * FROM " + table + " WHERE " + whereclause + " = ?";
            ps = conn.prepareStatement(sql);
            if (object instanceof Integer){
                ps.setInt(1,(int) object);
            }else if (object instanceof String){
                ps.setString(1,(String) object);
            }else if (object instanceof Double){
                ps.setDouble(1,(Double) object);
            }else{
                errPrint("new type is needed for whereclause");
                c.onError(new InputMismatchException());
                return;
            }

            resultset = ps.executeQuery();

            rsmd = resultset.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            Gson gson = new Gson();
            while (resultset.next()) {
                String json=makeJson(resultset,rsmd);
                print(json);
                t = gson.fromJson(json, (Class<T>) t.getClass());
                tlist.add(t);
            }
            c.onComplete(tlist);
        } catch (Exception e) {
            e.printStackTrace();
            c.onError(e);
        }finally {
            closeConnection();
        }
    }

    public void delete(Object o, String table, String columm, ModelCallback c) {
        conn=Singleton.getInstance().openDatabase();
        boolean checkTransaction = false;
        try {
            checkTransaction = conn.getAutoCommit();
            if(checkTransaction) {
                conn.setAutoCommit(false);
            }
            String sql = "delete from " + table + " where " + columm + " = ?";

            ps = (conn).prepareStatement(sql);

            if (o instanceof Integer){
                ps.setInt(1,(int) o);
            }else if (o instanceof String){
                ps.setString(1,(String) o);
            }else if (o instanceof Double){
                ps.setDouble(1,(Double) o);
            }else{
                errPrint("new type is needed for whereclause");
                c.onError(new InputMismatchException());
                return;
            }
            ps.executeUpdate();
            if(checkTransaction) {
                conn.commit();
            }
            c.onComplete(new Object());
        }catch(SQLException e) {
            e.printStackTrace();
            c.onError(e);
            if (conn != null) {
                try {
                    errPrint("Transaction is being rolled back");
                    if (checkTransaction) {
                        conn.rollback();
                    }
                } catch (SQLException excep) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            c.onError(e);
        } finally {
            try {
                if(checkTransaction) {
                    conn.setAutoCommit(true);
                }
                closeConnection();
            }catch (Exception e){
                e.printStackTrace();
                c.onError(e);
            }
        }
    }
    //id has to be present
    public void update(T t, String table, ModelCallback c) {
        conn=Singleton.getInstance().openDatabase();
        List<String> columns = new ArrayList<String>();
        int length = 0;
        boolean checkTransaction = false;
        try {
            checkTransaction = conn.getAutoCommit();
            String sql = "SELECT * FROM " + table;
            ps = conn.prepareStatement(sql);
            resultset = ps.executeQuery();
            rsmd = resultset.getMetaData();
            length = rsmd.getColumnCount();
            resultset.next();
            for (int i = 0; i < length; i++) {
                columns.add(i, rsmd.getColumnName(i + 1));
            }
            sql = "update " + table + " Set";
            for (int i = 1; i < columns.size(); i++) {
                if (i == columns.size() - 1) {
                    sql = sql + " " + columns.get(i) + " = ?";
                } else {
                    sql = sql + " " + columns.get(i) + " = ?,";
                }

            }
            sql = sql + " where " + columns.get(0) + " = ?";
            print(sql);
            Gson gson = new Gson();
            String json = gson.toJson(t);
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            print(jsonObject.toString());

            if (checkTransaction) {
                conn.setAutoCommit(false);
            }
            ps = this.conn.prepareStatement(sql);
            fromJson(ps, rsmd, jsonObject);
            ps.executeUpdate();
            if (checkTransaction) {
                conn.commit();
            }
            c.onComplete(new Object());
        }catch(SQLException e) {
            e.printStackTrace();
            c.onError(e);
            if (conn != null) {
                try {
                    errPrint("Transaction is being rolled back");
                    if (checkTransaction) {
                        conn.rollback();
                    }
                } catch (SQLException excep) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            c.onError(e);
        } finally {
            try {
                if(checkTransaction) {
                    conn.setAutoCommit(true);
                }
                closeConnection();
            }catch (Exception e){
                e.printStackTrace();
                c.onError(e);
            }
        }

    }

    public void getTableData(String table , ModelCallback<List<Object>> c) {
        conn=Singleton.getInstance().openDatabase();
        conn=Singleton.getInstance().openDatabase();
        String sql = "SELECT*FROM " + table;
        List<Object> data = new ArrayList<Object>();
        try {
            print(sql);
            ps = (conn).prepareStatement(sql);
            resultset = ps.executeQuery();

            rsmd = resultset.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            Gson gson = new Gson();

            while (resultset.next()) {
                String json=makeJson(resultset,rsmd);
                print(json);
                t = gson.fromJson(json, (Class<T>) t.getClass());
                data.add(t);
            }
            c.onComplete(data);
        } catch (Exception e) {
            e.printStackTrace();
            c.onError(e);
        }
        closeConnection();
    }
    public void checkIfAlreadyThere(String uniqueName,String table, String column, ModelCallback<Boolean> c){
        conn=Singleton.getInstance().openDatabase();
        try {
            String sql = "SELECT*FROM " + table + " WHERE " + column + " = ?";
            print(sql);
            ps = (conn).prepareStatement(sql);
            ps.setString(1,uniqueName);
            resultset = ps.executeQuery();
            if (!resultset.isBeforeFirst()) {
                print(resultset.isAfterLast()+"");
                c.onComplete(false);
            }else{
                c.onComplete(true);
            }
        }catch (SQLException se){
            se.printStackTrace();
            c.onError(se);
        }catch (Exception e){
            e.printStackTrace();
            c.onError(e);
        }
    }
    protected Object[] insertIntoObject(ResultSet rs, ResultSetMetaData){
        Object[] o = null;
        Field[] field = t.getClass().getDeclaredFields();
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                for (int  j = 0; j < field.length; j++){
                    if (rsmd.getColumnName(i).toLowerCase()==field[j].getName()){
                        if (rsmd.getColumnType(i) == Types.INTEGER) {
                            o[j] = rs.getInt(i);
                        } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
                            o[j] = rs.getString(i);
                        } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
                            o[j] = rs.getDouble(i);
                        } else if (rsmd.getColumnType(i) == Types.CHAR) {
                            o[j] = rs.getString(i);
                        } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
                            o[j] = rs.getBoolean(i);
                        } else {
                            errPrint("Add new type corresponding to the sql type"+ rsmd.getColumnType(i));
                            return null;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }
    protected void insertIntoDatabase(PreparedStatement ps, ResultSetMetaData rsmd, T t){
        try{
            for (Field field : t.getClass().getDeclaredFields()){
                for(int i=0;i <= rsmd.getColumnCount();i++){
                    if (rsmd.getColumnName(i)==field.getName()){
                        if (rsmd.getColumnType(i)==Types.INTEGER) {
                            ps.setInt(i, (int) runGetter(field, t));
                        } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
                            ps.setString(i, (String) runGetter(field, t));
                        } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
                            ps.setDouble(i, (double) runGetter(field, t));
                        } else if (rsmd.getColumnType(i) == Types.CHAR) {
                            ps.setString(i, (String) runGetter(field, t));
                        } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
                            ps.setBoolean(i, (Boolean) runGetter(field, t));
                        } else {
                            errPrint("Add new type corresponding to the sql type" + rsmd.getColumnType(i));
                            return;
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (resultset != null) {
                resultset.close();
            }
            if(conn != null){
                Singleton.getInstance().closeDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void print(String s){
        System.out.println(LOG_TAG+" : "+s);
    }
    protected void errPrint(String s){
        System.err.println(LOG_TAG+" : "+s);
    }
    public Object runGetter(Field field, T t) {
        // MZ: Find the correct method
        for (Method method : t.getClass().getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                        return method.invoke(t);
                    }catch (IllegalAccessException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }catch (InvocationTargetException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }
                }
            }
        }
        return null;
    }
    public  Object runSetter(Field field, Object[] o) {

        Constructor constructor = null;
        try {
            constructor = t.getClass().getConstructor();
        }catch (NoSuchMethodException nsme) {
            //handle constructor not being found
        }
        //try instantiating and returning
        try {
            return constructor.newInstance(o);
        }catch (InstantiationException ie) {
            //handle InstantiationException
        }catch (IllegalAccessException iae) {
            //handle IllegalAccessException
        }catch (InvocationTargetException ite) {
            //handle InvocationTargetException
        }
    }
    */

}
