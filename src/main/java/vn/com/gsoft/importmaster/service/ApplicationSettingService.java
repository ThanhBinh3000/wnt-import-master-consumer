package vn.com.gsoft.importmaster.service;

import vn.com.gsoft.importmaster.entity.ApplicationSetting;

import java.util.List;
import java.util.Map;

public interface ApplicationSettingService {
    Map<String, Object> getDrugStoreSetting(String maNhaThuoc);

    List<ApplicationSetting> getAppSettings(String maNhaThuoc);

    List<ApplicationSetting> getAppSettings(String maNhaThuoc, boolean excludeReadOnlyKey);
}
