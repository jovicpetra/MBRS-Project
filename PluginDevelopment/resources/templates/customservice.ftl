package ${app_name}.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class CustomGenericService<T,ID> {
@Autowired
protected JpaRepository<T, ID> repository;

public T save(T item) {
return repository.save(item);
}

public List<T> findAll() {
return repository.findAll();
}

public T findById(ID id) {
return repository.findById(id).get();
}

public T update(T item) {
return repository.save(item);
}

public void delete(ID id) {
repository.deleteById(id);
}
}