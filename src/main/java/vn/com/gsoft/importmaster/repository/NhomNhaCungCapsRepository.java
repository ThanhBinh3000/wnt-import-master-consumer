package vn.com.gsoft.importmaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.importmaster.entity.NhomNhaCungCaps;
import vn.com.gsoft.importmaster.model.dto.NhomNhaCungCapsReq;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhomNhaCungCapsRepository extends BaseRepository<NhomNhaCungCaps, NhomNhaCungCapsReq, Long> {
//    List<NhaThuocs> findByUserId(Long id);

    @Query("SELECT c FROM NhomNhaCungCaps c " +
            "WHERE 1=1 "
            + " AND (:#{#param.tenNhomNhaCungCap} IS NULL OR lower(c.tenNhomNhaCungCap) LIKE lower(concat('%',CONCAT(:#{#param.tenNhomNhaCungCap},'%'))))"
            + " AND (:#{#param.ghiChu} IS NULL OR lower(c.ghiChu) LIKE lower(concat('%',CONCAT(:#{#param.ghiChu},'%'))))"
            + " AND (:#{#param.maNhaThuoc} IS NULL OR lower(c.maNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.maNhaThuoc},'%'))))"
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
            + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " ORDER BY c.tenNhomNhaCungCap asc"
    )
    Page<NhomNhaCungCaps> searchPage(@Param("param") NhomNhaCungCapsReq param, Pageable pageable);


    @Query("SELECT c FROM NhomNhaCungCaps c " +
            "WHERE 1=1 "
            + " AND (:#{#param.tenNhomNhaCungCap} IS NULL OR lower(c.tenNhomNhaCungCap) LIKE lower(concat('%',CONCAT(:#{#param.tenNhomNhaCungCap},'%'))))"
            + " AND (:#{#param.ghiChu} IS NULL OR lower(c.ghiChu) LIKE lower(concat('%',CONCAT(:#{#param.ghiChu},'%'))))"
            + " AND (:#{#param.maNhaThuoc} IS NULL OR lower(c.maNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.maNhaThuoc},'%'))))"
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
            + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " ORDER BY c.tenNhomNhaCungCap asc"
    )
    List<NhomNhaCungCaps> searchList(@Param("param") NhomNhaCungCapsReq param);
    @Query("SELECT c FROM NhomNhaCungCaps c " +
            "WHERE 1=1 "
            + " AND (:#{#tenNhomNhaCc} IS NULL OR lower(c.tenNhomNhaCungCap) LIKE lower(concat('%',CONCAT(:#{#tenNhomNhaCc},'%'))))"
            + " AND (:#{#maNhaThuoc} IS NULL OR lower(c.maNhaThuoc) LIKE :#{#maNhaThuoc})"
            + " ORDER BY c.tenNhomNhaCungCap asc"
    )
    Optional<NhomNhaCungCaps> findByTenNhomNhaCungCapAndMaNhaThuoc(String tenNhomNhaCc, String maNhaThuoc);
}