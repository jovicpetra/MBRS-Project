package ${package}.enums;

public enum ${enum.name} {
    <#list enum.values as value>
    ${value}<#if !value?is_last>,</#if>
    </#list>
}