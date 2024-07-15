package vn.com.gsoft.importmaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.importmaster.entity.ReplaceGoodsAndBundleGoods;
import vn.com.gsoft.importmaster.model.dto.ReplaceGoodsAndBundleGoodsReq;

import java.util.List;

@Repository
public interface ReplaceGoodsAndBundleGoodsRepository extends BaseRepository<ReplaceGoodsAndBundleGoods, ReplaceGoodsAndBundleGoodsReq, Long> {
    @Query("SELECT c FROM ReplaceGoodsAndBundleGoods c " +
            " ORDER BY c.id desc"
    )
    Page<ReplaceGoodsAndBundleGoods> searchPage(@Param("param") ReplaceGoodsAndBundleGoodsReq param, Pageable pageable);


    @Query("SELECT c FROM ReplaceGoodsAndBundleGoods c " +
            " ORDER BY c.id desc"
    )
    List<ReplaceGoodsAndBundleGoods> searchList(@Param("param") ReplaceGoodsAndBundleGoodsReq param);
    List<ReplaceGoodsAndBundleGoods> findReplaceGoodsAndBundleGoodsByDrugStoreCodeAndDrugIdMap(String drugStoreCode, Long drugIdMap);
    List<ReplaceGoodsAndBundleGoods> findReplaceGoodsAndBundleGoodsByDrugStoreCodeAndDrugIdMapAndTypeId(String drugStoreCode, Long drugIdMap, Long typeId);
}
