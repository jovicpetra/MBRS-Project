package ${app_name}.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class CustomGenericService<T,ID> {
protected final JpaRepository<T, ID> repository;

public CustomGenericService(JpaRepository<T, ID> repository) {
this.repository = repository;
}

public T save(T item) {
return repository.save(item);
}

public List<T> findAll() {
return repository.findAll();
}

public T findById(ID id) {
return repository.findById(id).orElse(null);
}

public T update(T item) {
return repository.save(item);
}

public void delete(ID id) {
repository.deleteById(id);
}
}