package ${packageName};

import ${modelPackage}.${entityName};
import ${repositoryPackage}.${entityName}Repository;
import ${servicePackage}.${entityName}Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ${entityName}ServiceTest {

    private static final ${idFieldType} DEFAULT_ID = ${idFieldSampleValue};

    @Mock
    private ${entityName}Repository ${entityNameLower}Repository;

    private ${entityName}Service ${entityNameLower}Service;

    @BeforeEach
    void setUp() {
        ${entityNameLower}Service = new ${entityName}Service(${entityNameLower}Repository);
    }

    @Test
    void findAll_whenRepositoryHasData_returnsEntities() {
        ${entityName} entity = createEntity();
        when(${entityNameLower}Repository.findAll()).thenReturn(Arrays.asList(entity));

        List<${entityName}> result = ${entityNameLower}Service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findById_whenEntityExists_returnsEntity() {
        ${entityName} entity = createEntity();
        when(${entityNameLower}Repository.findById(DEFAULT_ID)).thenReturn(Optional.of(entity));

        ${entityName} result = ${entityNameLower}Service.findById(DEFAULT_ID);

        assertNotNull(result);
    }

    @Test
    void findById_whenEntityDoesNotExist_returnsNull() {
        when(${entityNameLower}Repository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertNull(${entityNameLower}Service.findById(DEFAULT_ID));
    }

    @Test
    void save_whenEntityIsValid_persistsEntity() {
        ${entityName} entity = createEntity();
        when(${entityNameLower}Repository.save(entity)).thenReturn(entity);

        ${entityName} result = ${entityNameLower}Service.save(entity);

        assertNotNull(result);
        verify(${entityNameLower}Repository, times(1)).save(entity);
    }

    @Test
    void delete_whenIdExists_deletesEntity() {
        ${entityNameLower}Service.delete(DEFAULT_ID);

        verify(${entityNameLower}Repository, times(1)).deleteById(DEFAULT_ID);
    }

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
