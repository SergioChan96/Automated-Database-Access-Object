
# Automatic Database Access Class

This tool is a class helping with Database Access to make it as easy as possible. For now it is a bit cumbersome to use but the plan is to integrate it like an API.
This class has dependencies with the GSON library and the SQLite JDBC. It is written with an MVP architecture in mind so it will always call the ModelCallback function.
The goal is to bring this tool to multiple database systems and to give a class which has all the columns of one entry in the whole database. 

## How does it work

 

## Usage

Drop these files into the source folder where all the DataAccessObjects are and simply extend from the abstract class DAO.java.
DAO expects the class that you want to put into the database as a constructor. ModelCallback will be expected for all these functions. 

The functions that are present are: 
- Insertion

The function `insert()` expectes the table name, the object that should be inserted, the columns of the table in an string array and a ModelCallback

- Select with the where clause
- Delete by ID
- Update an entry by ID
- Get the whole table
- Check if an entry is already there
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

## TODOs 

- phase out dependencies 
- Eliminate need for the interface DatabaseTable and the columns in function
- Compatability with other Databases
- covering more SQL commands
- One Class which contains everything of one object

## 
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)

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