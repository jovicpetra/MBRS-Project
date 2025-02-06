package ${app_name}.models.dto;
import java.util.*;
import ${app_name}.models.*;
import ${app_name}.enums.*;

public class ${class.name}DTO {
   <#list persistentProperties as property>
    <#if property??>
    ${property.visibility} <#if (property.type)=="date">Date<#else>${property.type}</#if> ${property.name};
    </#if>
    </#list>
    <#list referencedProperties as property>
    <#if property?? && property.connectionType == "MANY_TO_ONE">
    private ${property.type}DTO ${property.name};
    </#if>
    </#list>
    public ${class.name}DTO() { }

    <#list persistentProperties as property>
    public <#if (property.type)=="date">Date<#else>${property.type}</#if> get${property.name?cap_first}() { return ${property.name}; }

    public void set${property.name?cap_first}(<#if (property.type)=="date">Date<#else>${property.type}</#if> ${property.name}) { this.${property.name} = ${property.name}; }
    </#list>

    <#list referencedProperties as property>
    <#if property?? && property.connectionType == "MANY_TO_ONE">
    public ${property.type}DTO get${property.name?cap_first}() { return ${property.name}; }

    public void set${property.name?cap_first}(${property.type}DTO ${property.name}) { this.${property.name} = ${property.name}; }
    </#if>
    </#list>
}
