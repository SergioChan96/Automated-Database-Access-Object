package tools;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

// IMPORTANT: Always have the data in the same sequence as they appear in the database
public  abstract  class DAO<T> {
    protected String LOG_TAG = this.getClass().getName();
    protected Connection conn = null;
    protected PreparedStatement ps = null;
    protected ResultSet resultset = null;
    protected ResultSetMetaData rsmd = null;
    protected T t;
    protected String table;


    public DAO(T t, String table){
        this.t = t;
        this.table = table;
    }

    public void insertInto(Object t, ModelCallback c) {

    //getting the Metadata of the table for insertion
    String sql="";
    try {
        conn = Singleton.getInstance().openDatabase();
        String sql1 = "SELECT * FROM " + table;
        ps = conn.prepareStatement(sql1);
        resultset = ps.executeQuery();
        rsmd = resultset.getMetaData();
        resultset.next();
        int count = rsmd.getColumnCount();
        // checking if the database has an auto filled id field
        boolean id = false;
        for (int i = 0; i<count-1; i++) {
            if (rsmd.getColumnName(i).toLowerCase() == "id") {
                id=true;
            }
        }
        if (id) {
            for (int i = 0; i < count - 2; i++) {
                sql = sql + ",?";
            }
        }else {
            for (int i = 0; i < count - 1; i++) {
                sql = sql + ",?";
            }
        }
        }catch(Exception e){
            e.printStackTrace();
            c.onError(e);
        }
        sql = sql + ")";
        print(sql);
        boolean checkTransaction = false;
        try {

            checkTransaction = conn.getAutoCommit();
            if (checkTransaction) {
                conn.setAutoCommit(false);
            }
            ps = this.conn.prepareStatement(sql);
            insertIntoDatabase(ps, rsmd, this.t);
            ps.executeUpdate();
            if (checkTransaction) {
                conn.commit();
            }
            c.onComplete(new Object());
        } catch (SQLException e) {
            e.printStackTrace();
            c.onError(e);
            if (conn != null) {
                try {
                    errPrint("Transaction is being rolled back");
                    if (checkTransaction) {
                        conn.rollback();
                    }
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            c.onError(e);
        } finally {
            try {
                if (checkTransaction) {
                    conn.setAutoCommit(true);
                }
                closeConnection();
            } catch (Exception e) {
                closeConnection();
                e.printStackTrace();
                c.onError(e);
            }
        }
    }



    public void selectData(Object whereObject, String whereclause, ModelCallback<List<T>> c) {
        conn=Singleton.getInstance().openDatabase();
        List<T> tlist = new ArrayList<T>();
        try {
            String sql = "SELECT * FROM " + table + " WHERE " + whereclause + " = ?";
            ps = conn.prepareStatement(sql);
            if (whereObject instanceof Integer){
                ps.setInt(1,(int) whereObject);
            }else if (whereObject instanceof String){
                ps.setString(1,(String) whereObject);
            }else if (whereObject instanceof Double){
                ps.setDouble(1,(Double) whereObject);
            }else{
                errPrint("new type is needed for whereclause");
                c.onError(new InputMismatchException());
                return;
            }

            resultset = ps.executeQuery();

            rsmd = resultset.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultset.next()) {
                t=insertIntoObject(t ,resultset,rsmd);
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


    public void deleteData(Object whereObject, String whereclause, ModelCallback c) {
        conn=tools.Singleton.getInstance().openDatabase();
        boolean checkTransaction = false;
        try {
            checkTransaction = conn.getAutoCommit();
            if(checkTransaction) {
                conn.setAutoCommit(false);
            }
            String sql = "delete from " + table + " where " + whereclause + " = ?";

            ps = (conn).prepareStatement(sql);

            if (whereObject instanceof Integer){
                ps.setInt(1,(int) whereObject);
            }else if (whereObject instanceof String){
                ps.setString(1,(String) whereObject);
            }else if (whereObject instanceof Double){
                ps.setDouble(1,(Double) whereObject);
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
    /*
    //id has to be present
    public void update(T t, String table, tools.ModelCallback c) {
        conn=tools.Singleton.getInstance().openDatabase();
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

    public void getTableData(String table , tools.ModelCallback<List<Object>> c) {
        conn=tools.Singleton.getInstance().openDatabase();
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
    public void checkIfAlreadyThere(String uniqueName,String table, String column, tools.ModelCallback<Boolean> c){
        conn=tools.Singleton.getInstance().openDatabase();
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
    */
    protected T insertIntoObject(T t, ResultSet rs, ResultSetMetaData rsmd){
        Field[] field = t.getClass().getDeclaredFields();
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                for (int  j = 0; j < field.length; j++){
                    field[j].setAccessible(true);
                    if (rsmd.getColumnName(i).toLowerCase()==field[j].getName()){
                        if (rsmd.getColumnType(i) == Types.INTEGER) {
                            field[j].setInt(t,rs.getInt(i));
                        } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
                            field[j].set(t, rs.getString(i));
                        } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
                            field[j].setDouble(t,rs.getDouble(i));
                        } else if (rsmd.getColumnType(i) == Types.CHAR) {
                            field[j].set(t, rs.getString(i));
                        } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
                            field[j].setBoolean(t, rs.getBoolean(i));
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
        return t;
    }
    protected void insertIntoDatabase (PreparedStatement ps, ResultSetMetaData rsmd, T t){
        try {
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                for (int i = 0; i <= rsmd.getColumnCount(); i++) {
                    if (rsmd.getColumnName(i).toLowerCase() == field.getName().toLowerCase()) {
                        if (rsmd.getColumnType(i) == Types.INTEGER) {
                            ps.setInt(i, field.getInt(t));
                        } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
                            ps.setString(i, (String) field.get(t));
                        } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
                            ps.setDouble(i, field.getDouble(t));
                        } else if (rsmd.getColumnType(i) == Types.CHAR) {
                            ps.setString(i, (String) field.get(t));
                        } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
                            ps.setBoolean(i, field.getBoolean(t));
                        } else {
                            errPrint("Add new type corresponding to the sql type" + rsmd.getColumnType(i));
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeConnection () {
        try {
            if (resultset != null) {
                resultset.close();
            }
            if (conn != null) {
                Singleton.getInstance().closeDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void print (String s){
        System.out.println(LOG_TAG + " : " + s);
    }
    protected void errPrint (String s){
        System.err.println(LOG_TAG + " : " + s);
    }


}
