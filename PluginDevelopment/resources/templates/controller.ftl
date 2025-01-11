package ${class.typePackage}.controllers;

import ${class.typePackage}.models.*;
import ${class.typePackage}.models.dto.*;
import ${class.typePackage}.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/api/${class.name}")
public class ${class.name}Controller {

    @Autowired
    private ${class.name}Service ${class.name}Service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<${class.name}DTO>> getAll() {
        List<${class.name}> entityList = ${class.name}Service.findAll();
        if (entityList == null || entityList.isEmpty()) {
            return new ResponseEntity<List<${class.name}DTO>>(HttpStatus.BAD_REQUEST);
        }

        List<${class.name}DTO> dtoList = new ArrayList<>();
        for (${class.name} entity : entityList) {
            ${class.name}DTO dto = modelMapper.map(entity, ${class.name}DTO.class);
            dtoList.add(dto);
        }

        return new ResponseEntity<List<${class.name}DTO>>(dtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<${class.name}DTO> getById(@PathVariable Long id) {
        ${class.name} entity = ${class.name}Service.findOne(id);
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
                                                                 @PathVariable Long id) {
        if (id != dto.getId()) {
            return new ResponseEntity<${class.name}DTO>(HttpStatus.BAD_REQUEST);
        }

        ${class.name} updatedEntity = ${class.name}Service.update(modelMapper.map(dto, ${class.name}.class));
        if (updatedEntity == null) {
            return new ResponseEntity<${class.name}DTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<${class.name}DTO>(modelMapper.map(updatedEntity, ${class.name}DTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        ${class.name}Service.remove(id);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
