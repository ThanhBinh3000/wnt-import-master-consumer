package vn.com.gsoft.importmaster.service;


import vn.com.gsoft.importmaster.entity.PhieuNhaps;
import vn.com.gsoft.importmaster.entity.PhieuXuats;
import vn.com.gsoft.importmaster.model.dto.PhieuNhapsReq;

public interface PhieuNhapsService extends BaseService<PhieuNhaps, PhieuNhapsReq, Long> {


    PhieuNhaps createByPhieuXuats(PhieuXuats e);

    PhieuNhaps updateByPhieuXuats(PhieuXuats e);

    PhieuNhaps init(Integer maLoaiXuatNhap, Long id);
}