package vn.com.gsoft.inventory.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.inventory.entity.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {
    List<Department> findByUserId(Long id);
}
