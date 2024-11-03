package myplugin.generator.fmmodel;


public class PersistentProperty extends FMProperty {
    private String columnName;
    private Integer length;
    private Integer precision;
    private Strategy strategy;

    public PersistentProperty() {
		super();
	}

	public PersistentProperty(String name, String type, String visibility, int lower, int upper) {
		super(name, type, visibility, lower, upper);
	}
	
	public PersistentProperty(String columnName, Integer length, Integer precision, Strategy strategy) {
		this.columnName = columnName;
		this.length = length;
		this.precision = precision;
		this.strategy = strategy;
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

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}