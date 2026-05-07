package myplugin.generator.fmmodel;

public class UIProperty {

    private String label;
    private ComponentKind componentKind;
    private Boolean editable;
    private Boolean readOnly;
    private Boolean lookUp;
    private String presPropertyName;

    public UIProperty(String label, ComponentKind componentKind, Boolean editable,
                      Boolean readOnly, Boolean lookUp, String presPropertyName) {
        this.label = label;
        this.componentKind = componentKind;
        this.editable = editable;
        this.readOnly = readOnly;
        this.lookUp = lookUp;
        this.presPropertyName = presPropertyName;
    }

    /** Maps componentKind to an HTML input type string. */
    public String getFormType() {
        if (componentKind == null) return "text";
        switch (componentKind) {
            case NUMBER:   return "number";
            case PASSWORD: return "password";
            case DATE:     return "date";
            case CHECKBOX: return "checkbox";
            default:       return "text";
        }
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public ComponentKind getComponentKind() { return componentKind; }
    public void setComponentKind(ComponentKind componentKind) { this.componentKind = componentKind; }

    public Boolean getEditable() { return editable; }
    public void setEditable(Boolean editable) { this.editable = editable; }

    public Boolean getReadOnly() { return readOnly; }
    public void setReadOnly(Boolean readOnly) { this.readOnly = readOnly; }

    public Boolean getLookUp() { return lookUp; }
    public void setLookUp(Boolean lookUp) { this.lookUp = lookUp; }

    public String getPresPropertyName() { return presPropertyName; }
    public void setPresPropertyName(String presPropertyName) { this.presPropertyName = presPropertyName; }
}
