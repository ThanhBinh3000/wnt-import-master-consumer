package vn.com.gsoft.importmaster.service;

import vn.com.gsoft.importmaster.entity.NhaThuocs;
import vn.com.gsoft.importmaster.model.dto.NhaThuocsReq;

public interface NhaThuocsService extends BaseService<NhaThuocs, NhaThuocsReq, Long> {
    Boolean isInTheSameStoreChain(String firstStoreCode, String secondStoreCode);

}