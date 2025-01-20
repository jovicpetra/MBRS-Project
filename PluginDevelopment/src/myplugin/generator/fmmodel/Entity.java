package myplugin.generator.fmmodel;


public class Entity extends FMClass {
	private String tableName;

	public Entity(String name, String classPackage, String visibility, String tableName) {
		super(name, classPackage, visibility);
		this.tableName = tableName;
	}

	public Entity(String tableName) {
		super();
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}