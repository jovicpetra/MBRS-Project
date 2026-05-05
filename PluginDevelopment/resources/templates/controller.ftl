package ${app_name}.controllers;

import ${app_name}.models.*;
import ${app_name}.models.dto.*;
import ${app_name}.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/api/${class.name?lower_case}")
public class ${class.name}Controller {

    private final ${class.name}Service ${class.name?lower_case}Service;
    private final ModelMapper modelMapper;

    public ${class.name}Controller(${class.name}Service ${class.name?lower_case}Service, ModelMapper modelMapper) {
        this.${class.name?lower_case}Service = ${class.name?lower_case}Service;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<${class.name}DTO>> getAll() {
        List<${class.name}> entityList = ${class.name?lower_case}Service.findAll();
        if (entityList == null || entityList.isEmpty()) {
            return new ResponseEntity<List<${class.name}DTO>>(HttpStatus.BAD_REQUEST);
        }

        List<${class.name}DTO> dtoList = new ArrayList<${class.name}DTO>();
        for (${class.name} entity : entityList) {
            ${class.name}DTO dto = modelMapper.map(entity, ${class.name}DTO.class);
            dtoList.add(dto);
        }

        return new ResponseEntity<List<${class.name}DTO>>(dtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<${class.name}DTO> getById(@PathVariable ${idFieldType} id) {
        ${class.name} entity = ${class.name?lower_case}Service.findById(id);
        if (entity == null) {
            return new ResponseEntity<${class.name}DTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<${class.name}DTO>(modelMapper.map(entity, ${class.name}DTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<${class.name}DTO> create(@RequestBody @Valid ${class.name}DTO dto) {
        ${class.name?cap_first} createdEntity = ${class.name?lower_case}Service.save(modelMapper.map(dto, ${class.name}.class));
        if (createdEntity == null) {
            return new ResponseEntity<${class.name}DTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<${class.name}DTO>(modelMapper.map(createdEntity, ${class.name}DTO.class), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<${class.name}DTO> updateById(@RequestBody @Valid ${class.name}DTO dto,
                                                                 @PathVariable ${idFieldType} id) {
        if (!Objects.equals(id, dto.get${idFieldAccessor}())) {
            return new ResponseEntity<${class.name}DTO>(HttpStatus.BAD_REQUEST);
        }

        ${class.name} updatedEntity = ${class.name?lower_case}Service.update(modelMapper.map(dto, ${class.name}.class));
        if (updatedEntity == null) {
            return new ResponseEntity<${class.name}DTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<${class.name}DTO>(modelMapper.map(updatedEntity, ${class.name}DTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable ${idFieldType} id) {
        ${class.name?lower_case}Service.delete(id);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
}
