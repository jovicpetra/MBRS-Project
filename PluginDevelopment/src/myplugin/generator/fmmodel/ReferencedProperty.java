package myplugin.generator.fmmodel;


public class ReferencedProperty extends FMProperty {
    private FetchType fetch;
    private CascadeType cascade; // OneToMany
    private String columnName; // ManyToOne
    private String joinTable; // ManyToMany

    public ReferencedProperty() {
		super();
	}

	public ReferencedProperty(String name, String type, String visibility, int lower, int upper) {
		super(name, type, visibility, lower, upper);
	}
	
	public ReferencedProperty(FetchType fetch, CascadeType cascade, String columnName, String joinTable) {
		this.fetch = fetch;
		this.cascade = cascade;
		this.columnName = columnName;
		this.joinTable = joinTable;
	}

    public FetchType getFetch() {
        return fetch;
    }

    public void setFetch(FetchType fetch) {
        this.fetch = fetch;
    }

    public CascadeType getCascade() {
        return cascade;
    }

    public void setCascade(CascadeType cascade) {
        this.cascade = cascade;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }
}