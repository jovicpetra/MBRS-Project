package myplugin.generator.fmmodel;

public class UIClass {

    private String label;
    private Boolean create;
    private Boolean update;
    private Boolean delete;
    private Boolean view;

    public UIClass(String label, Boolean create, Boolean update, Boolean delete, Boolean view) {
        this.label = label;
        this.create = create;
        this.update = update;
        this.delete = delete;
        this.view = view;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Boolean getCreate() { return create; }
    public void setCreate(Boolean create) { this.create = create; }

    public Boolean getUpdate() { return update; }
    public void setUpdate(Boolean update) { this.update = update; }

    public Boolean getDelete() { return delete; }
    public void setDelete(Boolean delete) { this.delete = delete; }

    public Boolean getView() { return view; }
    public void setView(Boolean view) { this.view = view; }
}
