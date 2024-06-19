package vn.com.gsoft.importmaster.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.importmaster.entity.Inventory;

import java.util.Optional;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    Optional<Inventory> findByDrugStoreIdAndDrugIdAndRecordStatusId(String nhaThuocMaNhaThuoc, Long thuocThuocId, long active);
}
