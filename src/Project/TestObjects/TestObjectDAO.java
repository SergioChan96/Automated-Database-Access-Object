package Project.TestObjects;

import tools.DAO;
import tools.ModelCallback;

import java.util.List;

public class TestObjectDAO extends DAO {
    public TestObjectDAO(){
        super(new TestObject(), "test");
    }

    public void insert(TestObject o, ModelCallback c){
        insertInto(o,c);
    }
    public void select(Object whereobject, String whereColumn, ModelCallback<List<TestObject>> c){
        selectData(whereobject,whereColumn,c);
    }
    public void delete(Object whereobject, String whereColumn, ModelCallback c){
        deleteData(whereobject,whereColumn,c);
    }
    public void getAllData(ModelCallback<List<TestObject>> c){
        getTableData(c);
    }
    public void entryExistAlready(Object whereObject, String whereColumn, ModelCallback<Boolean> c){
        checkIfAlreadyThere(whereObject,whereColumn,c);
    }
}
