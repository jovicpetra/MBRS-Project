package myplugin.generator.fmmodel;


public class PersistentProperty extends FMProperty {
    private String columnName;
    private Integer length;
    private Integer precision;
    private boolean isId;
    private boolean unique;
    private boolean nullable;


	public PersistentProperty(String name, String type, String visibility, int lower, int upper) {
		super(name, type, visibility, lower, upper);
	}

    public PersistentProperty(String columnName, Integer length, Integer precision, boolean isId, boolean unique, boolean nullable) {
        this.columnName = columnName;
        this.length = length;
        this.precision = precision;
        this.isId = isId;
        this.unique = unique;
        this.nullable = nullable;
    }


    public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public boolean getIsId() {
        return isId;
    }

    public void setIsId(boolean id) {
        isId = id;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}