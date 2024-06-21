package vn.com.gsoft.importmaster.service;

import org.springframework.web.multipart.MultipartFile;
import vn.com.gsoft.importmaster.entity.NhaCungCaps;
import vn.com.gsoft.importmaster.model.dto.NhaCungCapsReq;

public interface NhaCungCapsService extends BaseService<NhaCungCaps, NhaCungCapsReq, Long> {
    NhaCungCaps save(String payload) throws Exception;

}