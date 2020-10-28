
# Automatic Database Access Class

This tool is a class helping with Database Access to make it as easy as possible. It'll communicate to the database for you, so no need to write SQL statements. For now it is a bit cumbersome to use but the plan is to integrate it like an API.
This class has dependencies with the GSON library and the SQLite JDBC. It is written with an MVP architecture in mind so it will always call the ModelCallback function.
The goal is to bring this tool to multiple database systems and to give a class which has all the columns of one entry in the whole database. 

## How does it work

The DAO takes your Object and maps its content to a JSON. This JSON gives its content one after another into the SQL statement which is why its important that all atributes are in the same order as the Database.
 
## Usage

Drop these files into the source folder where all the DataAccessObjects are and simply extend from the abstract class DAO.java. Write down all the tables and columns in them into an interface for easy use.
DAO expects the class that you want to put into the database as a constructor. ModelCallback aswell as the name of the table will be expected for all these functions

IMPORTANT the attributes of your classes that get put into the Database need to be in the same order as the columns in your table

The functions that are present are: 
- Insertion

The function `insert()` expectes the table name, the object that should be inserted, the columns of the table in an string array and a ModelCallback

- Select with the where clause

the where clause can be anything but the output will be an arraylist.
 
- Delete 

also has a where clause that can be anything. deletes everything that fits the where clause

- Update an entry by ID

The entry will just be overriden.

- Get the whole table
- Check if an entry is already there

like the select function with an boolean output.

- Callback 

```java
public void insert(table, user, columnNames , new ModelCallback() {
    @Override
    public void onComplete(Object o) {
        c.onComplete(o);
    }
    @Override
    public void onError(Exception e) {
        c.onError("Ein unerwarteter Fehler ist augetreten");
    }
});
```
Example of a whole class
```java
public class DaoUser extends Dao implements Tables.UserTable {
  
    public DaoUser() {
            super(new User());
    }
    public void insert(User user, ModelCallback c){
        insert(USER_TABLE,user,userAllColumns, c);
    }
    public void delete(User user, String column, ModelCallback c){
        delete(user,USER_TABLE,column,c);
    }
    public void select(User user, String column, ModelCallback<List<User>> c){
        select(user,USER_TABLE,column,c);
    }
    public void update(User user, ModelCallback c){
        update(user,USER_TABLE,c);
    }
    public void checkIfAlreadyThere(User user, ModelCallback<Boolean> c){
        checkIfAlreadyThere(user.getNickname(), USER_TABLE, COLUMN_NICKNAME, c);
    }

}
```
## TODOs 

- make delete function more clear
- phase out dependencies (get rid of JSON)
- Eliminate need for the interface DatabaseTable and the columns in function
- optimazation
- Compatability with other Databases
- covering more SQL commands
- One Class which contains everything of one object
