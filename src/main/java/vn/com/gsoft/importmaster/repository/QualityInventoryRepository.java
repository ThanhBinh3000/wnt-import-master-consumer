package vn.com.gsoft.importmaster.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.importmaster.entity.QualityInventory;

@Repository
public interface QualityInventoryRepository extends CrudRepository<QualityInventory, Long> {

}
