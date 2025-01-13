package ${app_name}.services;

import ${app_name}.repositories.${class.name}Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ${class.name}Service extends CustomGenericService<Company, Integer> {
@Autowired
private ${class.name}Repository ${class.name?lower_case}Repository;
}