import TestObjects.TestObject;

public class TestObjectDAO extends DAO {
    public TestObjectDAO(){
        super(new TestObject(), "test");
    }

    public void insert(TestObject o, ModelCallback c){
        insert(o,c);
    }
}
