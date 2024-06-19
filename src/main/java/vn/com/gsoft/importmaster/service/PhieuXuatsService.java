package vn.com.gsoft.importmaster.service;


import vn.com.gsoft.importmaster.entity.PhieuXuats;
import vn.com.gsoft.importmaster.model.dto.PhieuXuatsReq;

public interface PhieuXuatsService extends BaseService<PhieuXuats, PhieuXuatsReq, Long> {


    PhieuXuats init(Integer maLoaiXuatNhap, Long id) throws Exception;
}