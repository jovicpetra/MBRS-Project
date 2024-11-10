package ${class.typePackage}.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "${class.name?lower_case}")
${class.visibility} class ${class.name} {

    <#list persistentProperties as property>
    <#if property.isId?? && property.isId>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${property.columnName}", nullable = ${property.nullable?c}, unique = ${property.unique?c}, precision = ${property.precision})
    ${property.visibility} <#if (property.type)=="date">Date<#else>${property.type}</#if> ${property.name};
    </#list>

    public ${class.name}() { }

    <#list properties as property>
    <#if property.upper == 1>
    public ${property.type} get${property.name?cap_first}() { return ${property.name}; }

    public void set${property.name?cap_first}(${property.type} ${property.name}) { this.${property.name} = ${property.name}; }

    <#elseif property.upper == -1 >
    public Set<${property.type}> get${property.name?cap_first}() { return ${property.name}; }

    public void set${property.name?cap_first}(Set<${property.type}> ${property.name}) { this.${property.name} = ${property.name}; }

    <#else>
    <#list 1..property.upper as i>
    public ${property.type} get${property.name?cap_first}${i}() { return ${property.name}${i}; }

    public void set${property.name?cap_first}${i}(${property.type} ${property.name}${i}) { this.${property.name}${i} = ${property.name}${i}; }
    </#list>
    </#if>
    </#list>

}
