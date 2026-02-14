package ${app_name}.enums;

public enum ${enum.name} {
    <#list enum.values as value>
    ${value}<#if !value?is_last>,</#if>
    </#list>
}