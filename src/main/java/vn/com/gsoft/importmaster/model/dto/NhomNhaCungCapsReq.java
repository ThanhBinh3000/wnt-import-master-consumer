package vn.com.gsoft.importmaster.model.dto;

import lombok.Data;
import vn.com.gsoft.importmaster.model.system.BaseRequest;

@Data
public class NhomNhaCungCapsReq extends BaseRequest {

    private String tenNhomNhaCungCap;
    private String ghiChu;
    private String maNhaThuoc;
    private Boolean active;
    private Boolean isDefault;
    private Long archivedId;
    private Long storeId;


}
