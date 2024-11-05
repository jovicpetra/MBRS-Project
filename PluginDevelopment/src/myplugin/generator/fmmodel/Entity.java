package myplugin.generator.fmmodel;


public class Entity extends FMClass {
    private String tableName;

	/**
	 * @param name
	 * @param classPackage
	 * @param visibility
	 * @ToDo: add list of methods
	 */
	public Entity(String name, String classPackage, String visibility) {
		super(name, classPackage, visibility);
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}