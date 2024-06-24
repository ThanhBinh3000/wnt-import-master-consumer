package vn.com.gsoft.importmaster.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.importmaster.entity.KhachHangs;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangsRepository extends CrudRepository<KhachHangs, Long> {
    @Query("SELECT kh FROM KhachHangs kh where kh.customerTypeId=1 and kh.maNhaThuoc= ?1 ")
    Optional<KhachHangs> findKhachHangLe(String storeCode);
    @Query(value = "SELECT * FROM KhachHangs c WHERE c.soDienThoai = :phoneNumber " +
            "AND c.maNhaThuoc = :storeCode AND (:id IS NULL OR id != :id)", nativeQuery = true)
    List<KhachHangs> findCustomerByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("storeCode") String storeCode, @Param("id") Long id);
    //tìm khách hàng theo barcode
    @Query(value = "SELECT * FROM KhachHangs c WHERE c.barcode = :barcode AND c.maNhaThuoc = :storeCode" +
            " AND (:id IS NULL OR id != :id)", nativeQuery = true)
    List<KhachHangs> findCustomerByBarcode( @Param("barcode") String barcode,@Param("storeCode") String drugStoreCode, @Param("id") Long id);
    //tìm khách hàng theo mã khách hàng
    @Query(value = "SELECT * FROM KhachHangs c WHERE c.code = :code AND c.maNhaThuoc = :storeCode" +
            " AND (:id IS NULL OR id != :id)", nativeQuery = true)
    List<KhachHangs> findCustomerByCode( @Param("code") String code,@Param("storeCode") String storeCode, @Param("id") Long id);
}
