package vn.com.gsoft.inventory.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.inventory.constant.PathContains;
import vn.com.gsoft.inventory.model.dto.PhieuNhapsReq;
import vn.com.gsoft.inventory.model.system.BaseResponse;
import vn.com.gsoft.inventory.service.PhieuNhapsService;
import vn.com.gsoft.inventory.util.system.ResponseUtils;


@Slf4j
@RestController
@RequestMapping("/phieu-nhaps")
public class PhieuNhapsController {

    @Autowired
    PhieuNhapsService service;


    @PostMapping(value = PathContains.URL_SEARCH_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colection(@RequestBody PhieuNhapsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchPage(objReq)));
    }


    @PostMapping(value = PathContains.URL_SEARCH_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colectionList(@RequestBody PhieuNhapsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchList(objReq)));
    }

    @PostMapping(value = PathContains.URL_INIT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> init(@RequestParam(value = "maLoaiXuatNhap", required = true) Integer maLoaiXuatNhap,
                                             @RequestParam(value = "id", required = false) Long id) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.init(maLoaiXuatNhap, id)));
    }

    @PostMapping(value = PathContains.URL_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> insert(@Valid @RequestBody PhieuNhapsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.create(objReq)));
    }


    @PostMapping(value = PathContains.URL_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody PhieuNhapsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.update(objReq)));
    }


    @GetMapping(value = PathContains.URL_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.detail(id)));
    }


    @PostMapping(value = PathContains.URL_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@Valid @RequestBody PhieuNhapsReq idSearchReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.delete(idSearchReq.getId())));
    }
}
