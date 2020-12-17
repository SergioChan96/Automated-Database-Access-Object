
# Automatic Database Access Class

This tool is a class helping with Database Access to make it as easy as possible. It'll communicate to the database for you, so no need to write SQL statements.The plan is to intergrate it like an API. 
This class has dependencies with JDBC. It is written with an MVP architecture in mind, so it will always call the ModelCallback function.
The goal is to bring this tool to multiple database systems and to give a class which has all the columns of one entry in the whole database. 

## How does it work

The DAO takes your Object and reads out the content of the Object with reflection. With this it gets fed into the JDBC. It is important that the attributes in the class have the same name and order as they appear in the database.
 
## Usage

Drop these files into the source folder where all the DataAccessObjects are and simply extend from the abstract class DAO.java.
DAO expects the class that you want to put into the database and the table name as a constructor. ModelCallback will be expected for all the functions.

IMPORTANT: the attributes of your classes that get put into the Database need to be in the same order as the columns in your table

The functions that are present are: 
- Insertion

The function `insertInto()` expectes the object that should be inserted and a ModelCallback

- Select with the where clause

The function expects what your selecting for,  in what column and the ModelCallback. The output is an arraylist.
The column names are case sensitive.
 
- Delete 

The function expects what your selecting for,  in what column and the ModelCallback. Deletes everything that fits the where clause.
The column names are case sensitive.

- ~~Update an entry by ID~~

~~The entry will just be overriden.~~

- Get the whole table

This method give back the whole table in an arraylist.

- Check if an entry is already present.

The function expects what your selecting for,  in what column and the ModelCallback. This method outputs a boolean.

- Callback 

Example

```java
public void insertInto(user, new ModelCallback() {
    @Override
    public void onComplete(Object o) {
        ...
        Do stuff
        ...
    }
    @Override
    public void onError(Exception e) {
        ...
        Error handling
        ...
    }
});
```
Example of a whole class
```java
public class DaoUser extends Dao  {
  
    public DaoUser() {
            super(new User(), "TestDB");
    }
    public void insert(User user, ModelCallback c){
        insert(user, c);
    }
    public void delete(int userID, ModelCallback c){
        delete(userID, "ID",c);
    }
    public void select(String userName, ModelCallback<List<User>> c){
        select(userName, "name",c);
    }
    public void checkIfAlreadyThere(String userName, ModelCallback<Boolean> c){
        checkIfAlreadyThere(userName, "Name", c);
    }

}
```
## TODOs 

- ~~make delete function more clear~~
- ~~phase out dependencies (get rid of JSON)~~
- ~~Eliminate need for the interface DatabaseTable and the columns in function~~
- optimazation
- Compatability with other Databases
- Support with annotations
- covering more SQL commands
- One Class which contains everything of one object
