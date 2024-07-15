package vn.com.gsoft.importmaster.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.gsoft.importmaster.entity.BaseEntity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceGoodsAndBundleGoodsReq {
    private Long id;
    private Long drugId;
    private Long drugIdMap;
    private Long typeId;
    private String drugStoreCode;
}

