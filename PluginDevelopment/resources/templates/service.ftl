package ${app_name}.services;

import ${app_name}.repositories.${class.name}Repository;
import ${app_name}.models.${class.name};
import org.springframework.stereotype.Service;

@Service
public class ${class.name}Service extends CustomGenericService<${class.name}, ${idFieldType}> {

private final ${class.name}Repository ${class.name?lower_case}Repository;

public ${class.name}Service(${class.name}Repository ${class.name?lower_case}Repository) {
super(${class.name?lower_case}Repository);
this.${class.name?lower_case}Repository = ${class.name?lower_case}Repository;
}
}