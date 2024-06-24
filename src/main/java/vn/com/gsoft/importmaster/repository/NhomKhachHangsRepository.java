package vn.com.gsoft.importmaster.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.importmaster.entity.NhomKhachHangs;

import java.util.Optional;

@Repository
public interface NhomKhachHangsRepository extends CrudRepository<NhomKhachHangs, Long> {
    Optional<NhomKhachHangs> findByNhaThuocMaNhaThuocAndTenNhomKhachHang(String maNhaThuoc, String tenNhomKhachHang);
}
