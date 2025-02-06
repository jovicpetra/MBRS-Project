package ${app_name}.models;

import java.util.Date;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import ${app_name}.enums.*;

@Entity
@Table(name = "${entity.tableName}")
${class.visibility} class ${class.name} {

    <#list persistentProperties as property>
    <#if property.isId?? && property.isId>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${property.columnName}", nullable = ${property.nullable?c}, unique = ${property.unique?c}, precision = ${property.precision})
    ${property.visibility} <#if (property.type)=="date">Date<#else>${property.type}</#if> ${property.name};
    </#list>
    <#list referencedProperties as property>
    <#if property?? && property.connectionType == "ONE_TO_MANY">
    @OneToMany(mappedBy = "${property.mappedBy}", cascade = CascadeType.${property.cascade}, fetch = FetchType.${property.fetch})
    private List<${property.type}> ${property.name} = new ArrayList<>();
    <#elseif property?? && property.connectionType == "MANY_TO_ONE">
    @ManyToOne(fetch = FetchType.${property.fetch}<#if property.joinColumn == "client_id" && class.name == "Appointment">, cascade = CascadeType.ALL</#if>)
    @JoinColumn(name = "${property.joinColumn}")
    private ${property.type} ${property.name};
    </#if>
    </#list>

    public ${class.name}() { }

    <#list persistentProperties as property>
    public <#if (property.type)=="date">Date<#else>${property.type}</#if> get${property.name?cap_first}() { return ${property.name}; }

    public void set${property.name?cap_first}(<#if (property.type)=="date">Date<#else>${property.type}</#if> ${property.name}) { this.${property.name} = ${property.name}; }

    </#list>

    <#list referencedProperties as property>
    <#if property?? && property.connectionType == "ONE_TO_MANY">
    public List<${property.type}> get${property.name?cap_first}() { return ${property.name}; }

    public void set${property.name?cap_first}(List<${property.type}> ${property.name}) { this.${property.name} = ${property.name}; }

    <#elseif property?? && property.connectionType == "MANY_TO_ONE">
    public ${property.type} get${property.name?cap_first}() { return ${property.name}; }

    public void set${property.name?cap_first}(${property.type} ${property.name}) { this.${property.name} = ${property.name}; }

    </#if>
    </#list>

}
