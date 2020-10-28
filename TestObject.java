public class TestObject {
    private String string;
    private int integer;
    private double Double;

    public TestObject(){
    }

    public TestObject(String string, int integer, double Double){
        this.string = string;
        this.integer=integer;
        this.Double=Double;
    }
    public String getString() {
        return string;
    }

    public int getInteger() {
        return integer;
    }

    public double getDouble() {
        return Double;
    }

}
