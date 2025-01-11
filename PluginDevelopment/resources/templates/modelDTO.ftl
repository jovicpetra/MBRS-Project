package ${class.typePackage}.models.dto;

import ${class.typePackage}.enums; //this is the problem that I try to import
import java.util.Date;

public class ${class.name}DTO {

    private long id;
    private Date ${dateFieldName};
    private ${enumName} ${statusFieldName};

    // Getters
    public long getId() {
        return id;
    }

    public Date get${dateFieldName}() {
        return ${dateFieldName};
    }

    public ${enumName} get${statusFieldName}() {
        return ${statusFieldName};
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void set${dateFieldName}(Date ${dateFieldName}) {
        this.${dateFieldName} = ${dateFieldName};
    }

    public void set${statusFieldName}(${enumName} ${statusFieldName}) {
        this.${statusFieldName} = ${statusFieldName};
    }
}
