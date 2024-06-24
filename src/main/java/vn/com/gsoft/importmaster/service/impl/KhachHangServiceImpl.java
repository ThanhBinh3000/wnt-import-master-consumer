package vn.com.gsoft.importmaster.service.impl;

import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.importmaster.constant.ENoteType;
import vn.com.gsoft.importmaster.constant.RecordStatusContains;
import vn.com.gsoft.importmaster.entity.BacSies;
import vn.com.gsoft.importmaster.entity.KhachHangs;
import vn.com.gsoft.importmaster.entity.NhomKhachHangs;
import vn.com.gsoft.importmaster.entity.PhieuXuats;
import vn.com.gsoft.importmaster.model.dto.WrapDataBacSis;
import vn.com.gsoft.importmaster.model.dto.WrapDataKhachHangs;
import vn.com.gsoft.importmaster.model.system.Profile;
import vn.com.gsoft.importmaster.repository.BacSiesRepository;
import vn.com.gsoft.importmaster.repository.KhachHangsRepository;
import vn.com.gsoft.importmaster.repository.NhomKhachHangsRepository;
import vn.com.gsoft.importmaster.repository.PhieuXuatsRepository;
import vn.com.gsoft.importmaster.service.BacSiesService;
import vn.com.gsoft.importmaster.service.KhachHangService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class KhachHangServiceImpl implements KhachHangService {
    @Autowired
    private KhachHangsRepository khachHangsRepository;
    private PhieuXuatsRepository phieuXuatsRepository;
    private NhomKhachHangsRepository nhomKhachHangsRepository;
    @Override
    public void save(String payload) {
        Gson gson = new Gson();
        WrapDataKhachHangs dataKhachHangs = gson.fromJson(payload, WrapDataKhachHangs.class);
        List<KhachHangs> khachHangs = dataKhachHangs.getData();
        for (KhachHangs kh: khachHangs){
            kh.setMaNhomKhachHang(getThongTinIdNhomKhachHang(kh.getTenNhomKhachHang(),
                    dataKhachHangs.getProfile().getNhaThuoc().getMaNhaThuoc()));

            if(kh.getId()!= null && kh.getId() > 0){
                capNhatKhachHang(kh, dataKhachHangs.getProfile());
            }else {
                themMoiKhachHang(kh, dataKhachHangs.getProfile());
            }
        }
    }

    private KhachHangs themMoiKhachHang(KhachHangs khachHang, Profile userInfo) {
        var msg = "";
        if (khachHang.getSoDienThoai() != null && !khachHang.getSoDienThoai().trim().isEmpty()) {
            List<KhachHangs> customers = khachHangsRepository.findCustomerByPhoneNumber(khachHang.getSoDienThoai(), khachHang.getMaNhaThuoc(), null);
            if (!customers.isEmpty()) ;
        }
        if (khachHang.getCode() != null && !khachHang.getCode().trim().isEmpty()) {
            List<KhachHangs> customers = khachHangsRepository.findCustomerByCode(khachHang.getCode(), khachHang.getMaNhaThuoc(), null);
            if (!customers.isEmpty()) msg = "Mã khách hàng đã tồn tại";
        }
        if (khachHang.getBarCode() != null && !khachHang.getBarCode().trim().isEmpty()) {
            List<KhachHangs> customers = khachHangsRepository.findCustomerByBarcode(khachHang.getBarCode(), khachHang.getMaNhaThuoc(), null);
            if (!customers.isEmpty()) msg = "Mã vạch khách hàng đã tồn tại";
        }
        if(!msg.isEmpty()) return  khachHang;
        khachHangsRepository.save(khachHang);
        if (khachHang.getNoDauKy() != null && khachHang.getId() > 0) {
            if (khachHang.getNoDauKy().compareTo(BigDecimal.valueOf(0)) > 0) {
                taoPhieuDauKy(khachHang.getMaNhaThuoc(), khachHang.getId(), userInfo.getId(), khachHang.getNoDauKy().doubleValue(), userInfo.getNhaThuoc().getId());
            }
        }
        return khachHang;
    }

    private KhachHangs capNhatKhachHang(KhachHangs khachHang, Profile userInfo) {
        var msg = "";
        if (khachHang.getSoDienThoai() != null && !khachHang.getSoDienThoai().trim().isEmpty()) {
            List<KhachHangs> customers = khachHangsRepository.findCustomerByPhoneNumber(khachHang.getSoDienThoai(), khachHang.getMaNhaThuoc(), khachHang.getId());
            if (!customers.isEmpty()) ;
        }
        if (khachHang.getCode() != null && !khachHang.getCode().trim().isEmpty()) {
            List<KhachHangs> customers = khachHangsRepository.findCustomerByCode(khachHang.getCode(), khachHang.getMaNhaThuoc(), khachHang.getId());
            if (!customers.isEmpty()) msg = "Mã khách hàng đã tồn tại";
        }
        if (khachHang.getBarCode() != null && !khachHang.getBarCode().trim().isEmpty()) {
            List<KhachHangs> customers = khachHangsRepository.findCustomerByBarcode(khachHang.getBarCode(), khachHang.getMaNhaThuoc(), khachHang.getId());
            if (!customers.isEmpty()) msg = "Mã vạch khách hàng đã tồn tại";
        }
        if(!msg.isEmpty()) return khachHang;
        Optional<KhachHangs> optional = khachHangsRepository.findById(khachHang.getId());
        KhachHangs e = optional.get();
        BeanUtils.copyProperties(e, khachHang,
                "tenKhachHang",
                "maNhomKhachHang",
                "noDauKy",
                "code",
                "cusType",
                "taxCode",
                "diaChi",
                "soDienThoai",
                "email",
                "ghiChu",
                "birthDate");
        khachHangsRepository.save(khachHang);
        if (khachHang.getNoDauKy() != null) {
            if (khachHang.getNoDauKy().compareTo(BigDecimal.valueOf(0)) > 0) {
                taoPhieuDauKy(khachHang.getMaNhaThuoc(), khachHang.getId(), userInfo.getId(), khachHang.getNoDauKy().doubleValue(), userInfo.getNhaThuoc().getId());
            }
        }
        return khachHang;
    }

    private void taoPhieuDauKy(String storeCode, Long maKhachHang,
                               Long userId, Double tongTien, Long storeId) {
        //kiểm tra xem tồn tại phiếu chưa
        PhieuXuats phieuXuat = this.phieuXuatsRepository.findByNhaThuocMaNhaThuocAndKhachHangMaKhachHangAndMaLoaiXuatNhapAndRecordStatusId(
                storeCode,
                maKhachHang,
                ENoteType.InitialSupplierDebt,
                (int) RecordStatusContains.ACTIVE);
        if(phieuXuat != null && phieuXuat.getId() != null && phieuXuat.getId() > 0){
            phieuXuat.setRecordStatusId(tongTien > 0 ? RecordStatusContains.ACTIVE : RecordStatusContains.DELETED_FOREVER);
            phieuXuat.setTongTien(BigDecimal.valueOf(tongTien));
            phieuXuat.setIsDebt(tongTien > 0);
        }else {
            phieuXuat = new PhieuXuats();
            phieuXuat.setKhachHangMaKhachHang(maKhachHang);
            phieuXuat.setSoPhieuXuat(0L);
            phieuXuat.setRecordStatusId(RecordStatusContains.ACTIVE);
            phieuXuat.setCreated(new Date());
            phieuXuat.setCreatedByUserId(userId);
            phieuXuat.setTongTien(BigDecimal.valueOf(tongTien));
            phieuXuat.setIsDebt(true);
            phieuXuat.setMaLoaiXuatNhap((int) ENoteType.InitialSupplierDebt.longValue());
            phieuXuat.setNhaThuocMaNhaThuoc(storeCode);
            phieuXuat.setStoreId(storeId);
            phieuXuat.setTargetId(null);
            phieuXuat.setTargetStoreId(null);
            phieuXuat.setTargetManagementId(null);
            phieuXuat.setIsModified(false);
            phieuXuat.setBackPaymentAmount(new BigDecimal(0L));
            phieuXuat.setConnectivityStatusID(0L);
            phieuXuat.setDaTra(BigDecimal.valueOf(0d));
            phieuXuat.setDiscount(0F);
            phieuXuat.setPaymentScore(new BigDecimal(0L));
            phieuXuat.setVat(0);
        }
        phieuXuatsRepository.save(phieuXuat);
    }
    private Long  getThongTinIdNhomKhachHang(String tenNhomKhachHang, String maNhaThuoc){
        Optional<NhomKhachHangs> nhomKhachHang = nhomKhachHangsRepository.findByNhaThuocMaNhaThuocAndTenNhomKhachHang(
                maNhaThuoc,
                tenNhomKhachHang);
        if(nhomKhachHang.isPresent()){
            return nhomKhachHang.get().getId();
        }else {
            NhomKhachHangs nhomKhachHangs = new NhomKhachHangs();
            nhomKhachHangs.setTenNhomKhachHang(tenNhomKhachHang);
            nhomKhachHangs.setNhaThuocMaNhaThuoc(maNhaThuoc);
            nhomKhachHangs.setActive(true);
            nhomKhachHangs.setRecordStatusId(RecordStatusContains.ACTIVE);
            nhomKhachHangs.setArchivedId(0L);
            nhomKhachHangs.setStoreId(0L);
            nhomKhachHangs.setGroupTypeId(0L);
            nhomKhachHangsRepository.save(nhomKhachHangs);
            return nhomKhachHangs.getId();
        }
    }
}
