package vn.com.gsoft.importmaster.model.dto;

import lombok.Data;
import vn.com.gsoft.importmaster.model.system.Profile;

import java.util.Date;

@Data
public class WrapData<T> {
    private String code;
    private Date sendDate;
    private T data;
    private String bathKey;
    private Integer index;
    private Integer total;
    private Profile profile;
}
