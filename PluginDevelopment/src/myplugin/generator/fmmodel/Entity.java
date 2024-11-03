package myplugin.generator.fmmodel;


public class Entity extends FMClass {
    private String tableName;

    public Entity() {
		super();
	}

    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}