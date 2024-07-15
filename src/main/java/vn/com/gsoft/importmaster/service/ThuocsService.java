package vn.com.gsoft.importmaster.service;


import org.springframework.transaction.annotation.Transactional;
import vn.com.gsoft.importmaster.entity.Thuocs;
import vn.com.gsoft.importmaster.model.dto.ThuocsReq;

public interface ThuocsService extends BaseService<Thuocs, ThuocsReq, Long> {
    Thuocs save(String payload) throws Exception;

}
