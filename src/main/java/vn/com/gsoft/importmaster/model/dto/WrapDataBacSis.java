package vn.com.gsoft.importmaster.model.dto;

import lombok.Data;
import vn.com.gsoft.importmaster.entity.BacSies;
import vn.com.gsoft.importmaster.entity.PhieuXuats;

import java.util.Date;

@Data
public class WrapDataBacSis {
    private String code;
    private Date sendDate;
    private BacSies data;
    private String bathKey;
    private Integer index;
    private Integer total;
}
