package ${app_name}.models.dto;
import java.util.Date;

public class ${class.name}DTO {
   <#list persistentProperties as property>
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
