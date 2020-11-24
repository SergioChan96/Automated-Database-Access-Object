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
    public void select(Object whereobject, String whereclause, ModelCallback<List<TestObject>> c){
        selectData(whereobject,whereclause,c);
    }
    public void delete(Object whereobject, String whereclause, ModelCallback c){
        deleteData(whereobject,whereclause,c);
    }
}
