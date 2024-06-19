package vn.com.gsoft.importmaster.repository.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vn.com.gsoft.importmaster.model.system.BaseResponse;

@FeignClient(name = "wnt-security")
public interface UserProfileFeign {
    @GetMapping("/profile")
    BaseResponse getProfile();
}
