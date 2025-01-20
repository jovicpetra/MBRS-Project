package ${app_name}.repositories;

import ${app_name}.models.${class.name?cap_first};
import org.springframework.data.jpa.repository.JpaRepository;

public interface ${class.name?cap_first}Repository extends JpaRepository<${class.name?cap_first}, Integer> {

}