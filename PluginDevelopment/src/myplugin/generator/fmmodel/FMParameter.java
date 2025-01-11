package myplugin.generator.fmmodel;

public class FMParameter extends FMElement {
    private FMType type;

    public FMParameter(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public FMParameter(FMType type, String name) {
        super(name);
        this.type = type;
    }

    public FMParameter(String typeName, String typePackage, String name) {
        super(name);
        this.type = new FMType(typeName, typePackage);
    }

    public FMType getType() {
        return type;
    }

    public void setType(FMType type) {
        this.type = type;
    }

    public void setType(String name, String typePackage) {
        this.type = new FMType(name, typePackage);
    }
}
