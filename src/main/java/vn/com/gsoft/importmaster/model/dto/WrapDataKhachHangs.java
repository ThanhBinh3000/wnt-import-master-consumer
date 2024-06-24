package vn.com.gsoft.importmaster.model.dto;

import lombok.Data;
import vn.com.gsoft.importmaster.entity.BacSies;
import vn.com.gsoft.importmaster.entity.KhachHangs;
import vn.com.gsoft.importmaster.model.system.Profile;

import java.util.Date;
import java.util.List;

@Data
public class WrapDataKhachHangs {
    private String code;
    private Date sendDate;
    private List<KhachHangs> data;
    private String bathKey;
    private Integer index;
    private Integer total;
    private Profile profile;
}
