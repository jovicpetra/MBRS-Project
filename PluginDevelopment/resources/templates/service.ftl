package ${app_name}.services;

import ${app_name}.repositories.${class.name}Repository;
import ${app_name}.models.${class.name};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ${class.name}Service extends CustomGenericService<${class.name}, Integer> {
@Autowired
private ${class.name}Repository ${class.name?lower_case}Repository;
}