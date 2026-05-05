package ${packageName};

import ${modelPackage}.${entityName};
import ${dtoPackage}.${entityName}DTO;
import ${servicePackage}.${entityName}Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(${entityName}Controller.class)
class ${entityName}ControllerTest {

        private static final ${idFieldType} DEFAULT_ID = ${idFieldSampleValue};

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ${entityName}Service ${entityNameLower}Service;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void getAll_whenEntitiesExist_returnsOk() throws Exception {
        ${entityName} entity = createEntity();
                ${entityName}DTO dto = createDto(DEFAULT_ID);

        when(${entityNameLower}Service.findAll()).thenReturn(Arrays.asList(entity));
        when(modelMapper.map(entity, ${entityName}DTO.class)).thenReturn(dto);

        mockMvc.perform(get("/api/${entityNameLower}"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_whenEntityExists_returnsOk() throws Exception {
        ${entityName} entity = createEntity();
        ${entityName}DTO dto = createDto(DEFAULT_ID);

        when(${entityNameLower}Service.findById(DEFAULT_ID)).thenReturn(entity);
        when(modelMapper.map(entity, ${entityName}DTO.class)).thenReturn(dto);

        mockMvc.perform(get("/api/${entityNameLower}/{id}", DEFAULT_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getById_whenEntityDoesNotExist_returnsNotFound() throws Exception {
        when(${entityNameLower}Service.findById(DEFAULT_ID)).thenReturn(null);

        mockMvc.perform(get("/api/${entityNameLower}/{id}", DEFAULT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_whenRequestIsValid_returnsCreated() throws Exception {
        ${entityName} entity = createEntity();
        ${entityName}DTO dto = createDto(null);

        when(modelMapper.map(any(${entityName}DTO.class), any())).thenReturn(entity);
        when(${entityNameLower}Service.save(any(${entityName}.class))).thenReturn(entity);
        when(modelMapper.map(entity, ${entityName}DTO.class)).thenReturn(createDto(DEFAULT_ID));

        mockMvc.perform(post("/api/${entityNameLower}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateById_whenRequestIsValid_returnsOk() throws Exception {
        ${entityName} entity = createEntity();
                ${entityName}DTO dto = createDto(DEFAULT_ID);

        when(modelMapper.map(any(${entityName}DTO.class), any())).thenReturn(entity);
        when(${entityNameLower}Service.update(any(${entityName}.class))).thenReturn(entity);
        when(modelMapper.map(entity, ${entityName}DTO.class)).thenReturn(dto);

        mockMvc.perform(put("/api/${entityNameLower}/{id}", DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById_whenEntityExists_returnsNoContent() throws Exception {
        doNothing().when(${entityNameLower}Service).delete(DEFAULT_ID);

        mockMvc.perform(delete("/api/${entityNameLower}/{id}", DEFAULT_ID))
                .andExpect(status().isNoContent());
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

        private ${entityName}DTO createDto(${idFieldType} id) {
        ${entityName}DTO dto = new ${entityName}DTO();
                if (id != null) {
                        dto.set${idFieldAccessor}(id);
                }
<#list fields as field>
<#if field.isId != "true">
        dto.set${field.name?cap_first}(${field.sampleValue});
</#if>
</#list>
        return dto;
    }
}
