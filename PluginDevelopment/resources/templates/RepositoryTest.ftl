package ${packageName};

import ${modelPackage}.${entityName};
import ${repositoryPackage}.${entityName}Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ${entityName}RepositoryTest {

    @Autowired
    private ${entityName}Repository ${entityNameLower}Repository;

    @Test
    void saveAndFindById_whenEntityIsPersisted_returnsEntity() {
        ${entityName} entity = createEntity();

        ${entityName} saved = ${entityNameLower}Repository.save(entity);
        Optional<${entityName}> found = ${entityNameLower}Repository.findById(saved.get${idFieldAccessor}());

        assertTrue(found.isPresent());
        assertNotNull(found.get().get${idFieldAccessor}());
    }

<#if customQueryMethods?? && customQueryMethods?size gt 0>
<#list customQueryMethods as method>
    @Test
    void ${method.name}_whenCustomQueryInvoked_returnsResult() {
        ${entityNameLower}Repository.${method.name}(<#list method.params as p>${p.value}<#if p_has_next>, </#if></#list>);
        assertTrue(true);
    }

</#list>
</#if>
    private ${entityName} createEntity() {
        ${entityName} entity = new ${entityName}();
<#list fields as field>
        <#if field.isId != "true">
        entity.set${field.name?cap_first}(${field.sampleValue});
        </#if>
</#list>
    <#if referencedProperties??>
    <#list referencedProperties as ref>
    <#if ref.connectionType == "MANY_TO_ONE">
        entity.set${ref.name?cap_first}(new ${modelPackage}.${ref.type}());
    </#if>
    </#list>
    </#if>
        return entity;
    }
}
