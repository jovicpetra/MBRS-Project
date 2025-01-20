package myplugin.generator.fmmodel;


public class ReferencedProperty extends FMProperty {
    private FetchType fetch;
    private CascadeType cascade; // OneToMany
    private String mappedBy; // OneToMany
    private String joinTable; // ManyToMany
    private String joinColumn; //ManyToOne
    private ConnectionType connectionType;

    public ReferencedProperty() {
		super();
	}

	public ReferencedProperty(String name, String type, String visibility, int lower, int upper) {
		super(name, type, visibility, lower, upper);
	}
	
	public ReferencedProperty(FetchType fetch, CascadeType cascade, String mappedBy, String joinTable, String joinColumn, ConnectionType connectionType) {
		this.fetch = fetch;
		this.cascade = cascade;
		this.mappedBy = mappedBy;
		this.joinTable = joinTable;
        this.joinColumn = joinColumn;
        this.connectionType = connectionType;
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

    public String getMappedBy() {
        return mappedBy;
    }

    public void setMappedBy(String mappedBy) {
        this.mappedBy = mappedBy;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public String getJoinColumn() {
        return joinColumn;
    }

    public void setJoinColumn(String joinColumn) {
        this.joinColumn = joinColumn;
    }
}