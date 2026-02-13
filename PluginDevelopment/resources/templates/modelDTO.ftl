package ${app_name}.models.dto;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ${app_name}.models.*;
import ${app_name}.enums.*;

public class ${class.name}DTO {
   <#list persistentProperties as property>
    <#if property.isId?? && property.isId>
    </#if>
    ${property.visibility} <#if (property.type)=="date">Date<#else>${property.type}</#if> ${property.name};
    </#list>
    <#list referencedProperties as property>
    <#if property?? && property.connectionType == "ONE_TO_MANY">
    @JsonIgnore
    private List<${property.type}> ${property.name} = new ArrayList<>();
    <#elseif property?? && property.connectionType == "MANY_TO_ONE">
    private Long ${property.name}Id;
    </#if>
    </#list>

    public ${class.name}DTO() { }

      <#list persistentProperties as property>
      public <#if (property.type)=="date">Date<#else>${property.type}</#if> get${property.name?cap_first}() { return ${property.name}; }

      public void set${property.name?cap_first}(<#if (property.type)=="date">Date<#else>${property.type}</#if> ${property.name}) { this.${property.name} = ${property.name}; }

      </#list>

      <#list referencedProperties as property>
      <#if property?? && property.connectionType == "ONE_TO_MANY">
      public List<${property.type}> get${property.name?cap_first}() { return ${property.name}; }

      public void set${property.name?cap_first}(List<${property.type}> ${property.name}) { this.${property.name} = ${property.name}; }

      <#elseif property?? && property.connectionType == "MANY_TO_ONE">
      public Long get${property.name?cap_first}Id() { return ${property.name}Id; }

      public void set${property.name?cap_first}Id(Long ${property.name}Id) { this.${property.name}Id = ${property.name}Id; }
      </#if>
      </#list>


}
