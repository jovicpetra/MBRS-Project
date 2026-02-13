package ${app_name}.config;

import ${app_name}.models.*;
import ${app_name}.repositories.*;
import ${app_name}.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Initializes the database with sample data for development and testing purposes
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final Random random = new Random();

<#list classes as class>
    @Autowired
    private ${class.name}Repository ${class.name?uncap_first}Repository;

</#list>

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        <#if classes?has_content>
        if (${classes[0].name?uncap_first}Repository.count() > 0) {
            System.out.println("Database already contains data. Skipping initialization.");
            return;
        }
        </#if>

        System.out.println("Initializing database with sample data...");

<#list classes as class>
        // Create sample ${class.name} data
        create${class.name}Data();

</#list>
        System.out.println("Database initialization completed!");
    }

<#list classes as class>
    private void create${class.name}Data() {
        for (int i = 1; i <= 5; i++) {
            ${class.name} entity = new ${class.name}();
            
<#list class.persistentProperties as prop>
<#if !(prop.isId?? && prop.isId)>
<#if prop.type == "String">
            entity.set${prop.name?cap_first}("${class.name} ${prop.name} " + i);
<#elseif prop.type == "Integer" || prop.type == "int">
<#if prop.name?lower_case?contains("price") || prop.name?lower_case?contains("cost") || prop.name?lower_case?contains("amount")>
            entity.set${prop.name?cap_first}((i * 10) + random.nextInt(50)); // Random price
<#else>
            entity.set${prop.name?cap_first}(i);
</#if>
<#elseif prop.type == "Double" || prop.type == "double" || prop.type == "Float" || prop.type == "float">
            entity.set${prop.name?cap_first}(10.0 + (i * 5.5));
<#elseif prop.type == "Boolean" || prop.type == "boolean">
            entity.set${prop.name?cap_first}(i % 2 == 0);
<#elseif prop.type == "LocalDate">
            entity.set${prop.name?cap_first}(LocalDate.now().plusDays(i));
<#elseif prop.type == "LocalDateTime">
            entity.set${prop.name?cap_first}(LocalDateTime.now().plusDays(i));
<#elseif prop.type == "Date" || prop.type == "date">
            entity.set${prop.name?cap_first}(new java.util.Date());
<#else>
    <#assign enumMatched = false>
    <#if enumerations??>
        <#list enumerations as en>
            <#if prop.type == en.name>
                <#assign enumMatched = true>
                <#if en.values?has_content>
            entity.set${prop.name?cap_first}(${en.name}.${en.values?first});
                <#else>
            entity.set${prop.name?cap_first}(${en.name}.values()[0]);
                </#if>
            </#if>
        </#list>
    </#if>
    <#if !enumMatched>
            entity.set${prop.name?cap_first}(${prop.type}.values()[0]);
    </#if>
</#if>
</#if>
</#list>

<#if class.referencedProperties?has_content>
<#list class.referencedProperties as refProp>
    <#if refProp.connectionType == "MANY_TO_ONE" || refProp.connectionType == "ONE_TO_ONE">
            List<${refProp.type}> ${refProp.type?uncap_first}List = ${refProp.type?uncap_first}Repository.findAll();
            if (${refProp.type?uncap_first}List.isEmpty()) {
                create${refProp.type}Data();
                ${refProp.type?uncap_first}List = ${refProp.type?uncap_first}Repository.findAll();
            }
            if (!${refProp.type?uncap_first}List.isEmpty()) {
                entity.set${refProp.name?cap_first}(${refProp.type?uncap_first}List.get(0));
            }
    </#if>
</#list>
</#if>
            
            ${class.name?uncap_first}Repository.save(entity);
        }
        System.out.println("Created 5 sample ${class.name} records");
    }

</#list>
}
