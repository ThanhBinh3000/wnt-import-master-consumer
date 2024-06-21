package vn.com.gsoft.importmaster.service.impl;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.importmaster.constant.ENoteType;
import vn.com.gsoft.importmaster.constant.RecordStatusContains;
import vn.com.gsoft.importmaster.entity.NhaCungCaps;
import vn.com.gsoft.importmaster.entity.PhieuNhaps;
import vn.com.gsoft.importmaster.model.dto.NhaCungCapsReq;
import vn.com.gsoft.importmaster.model.dto.WrapData;
import vn.com.gsoft.importmaster.model.system.Profile;
import vn.com.gsoft.importmaster.repository.NhaCungCapsRepository;
import vn.com.gsoft.importmaster.repository.NhomNhaCungCapsRepository;
import vn.com.gsoft.importmaster.repository.PhieuNhapsRepository;
import vn.com.gsoft.importmaster.service.NhaCungCapsService;

import java.time.Instant;
import java.util.*;


@Service
@Log4j2
public class NhaCungCapsServiceImpl extends BaseServiceImpl<NhaCungCaps, NhaCungCapsReq,Long> implements NhaCungCapsService {

	//region Fields
	private NhaCungCapsRepository hdrRepo;
	private NhomNhaCungCapsRepository nhomNhaCungCapsRepository;
	private PhieuNhapsRepository phieuNhapsRepository;
	//endregion

	//region Constructor
	@Autowired
	public NhaCungCapsServiceImpl(NhaCungCapsRepository hdrRepo,
                                  NhomNhaCungCapsRepository nhomNhaCungCapsRepository,
                                  PhieuNhapsRepository phieuNhapsRepository) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
		this.nhomNhaCungCapsRepository = nhomNhaCungCapsRepository;
		this.phieuNhapsRepository = phieuNhapsRepository;
	}

	@Override
	public NhaCungCaps save(String payload) throws Exception{
		Gson gson = new Gson();
		TypeToken<WrapData<NhaCungCaps>> typeToken = new TypeToken<WrapData<NhaCungCaps>>() {};
		WrapData<NhaCungCaps> dataNhaCungCaps = gson.fromJson(payload, typeToken.getType());

		NhaCungCaps nhaCungCaps = dataNhaCungCaps.getData();
		var storeCode = dataNhaCungCaps.getProfile().getNhaThuoc().getMaNhaThuoc();
		nhaCungCaps.setMaNhaThuoc(storeCode);
		//kiểm tra tên nhà cung cấp có tồn tại chưa
		if(nhaCungCaps.getTenNhaCungCap() != null && !nhaCungCaps.getTenNhaCungCap().isEmpty()){
			var supplier = this.hdrRepo.findNhaCungCapByName(storeCode, nhaCungCaps.getTenNhaCungCap(), null);
			if(!supplier.isEmpty())
				throw  new Exception("Tên nhà cung cấp đã tồn tại");
		}
		//kiểm tra mã vạch
		if(nhaCungCaps.getBarCode() != null && !nhaCungCaps.getBarCode().isEmpty()){
			var supplier = this.hdrRepo.findNhaCungCapByBarcode(storeCode, nhaCungCaps.getBarCode(), null);
			if(!supplier.isEmpty())
				throw new Exception("Mã vạch đã tồn tại");
		}
		//kiểm tra mã
		if(nhaCungCaps.getCode() != null && !nhaCungCaps.getCode().isEmpty()){
			var supplier = this.hdrRepo.findNhaCungCapByCode(storeCode, nhaCungCaps.getCode(), null);
			if(!supplier.isEmpty())
				throw new Exception("Mã nhà cung cấp đã tồn tại");
		}
		if(nhaCungCaps.getTenNhomNhaCungCaps() != null && !nhaCungCaps.getTenNhomNhaCungCaps().isEmpty()){
			var supplierGroup = this.nhomNhaCungCapsRepository.findByTenNhomNhaCungCapAndMaNhaThuoc(nhaCungCaps.getTenNhomNhaCungCaps(), dataNhaCungCaps.getProfile().getNhaThuoc().getMaNhaThuoc());
			if(supplierGroup.isEmpty()){
				throw new Exception("Mã nhóm nhà cung cấp không tồn tại");
			}else{
				nhaCungCaps.setMaNhomNhaCungCap(supplierGroup.get().getId().intValue());
			}
		}
		NhaCungCaps e = new NhaCungCaps();
		BeanUtils.copyProperties(nhaCungCaps, e, "id");
		e.setCreated(Date.from(Instant.now()));
		e.setCreatedByUserId(dataNhaCungCaps.getProfile().getId());
		e.setRecordStatusId(RecordStatusContains.ACTIVE);
		e.setIsOrganization(0);
		e.setSupplierTypeId(0);
		e = hdrRepo.save(e);
		if (e.getNoDauKy() != null && e.getId() > 0){
			taoPhieuDauKy(storeCode, e.getId(), dataNhaCungCaps.getProfile().getId(), e.getNoDauKy().doubleValue(), dataNhaCungCaps.getProfile().getNhaThuoc().getId());
		}
		return e;
	}
	private void taoPhieuDauKy(String storeCode, Long maNhaCungCap,
							   Long userId, Double tongTien, Long storeId) throws Exception{
		PhieuNhaps phieuNhaps = this.phieuNhapsRepository.findByNhaThuocMaNhaThuocAndNhaCungCapMaNhaCungCapAndLoaiXuatNhapMaLoaiXuatNhapAndRecordStatusId(
				storeCode,
				maNhaCungCap,
				ENoteType.InitialSupplierDebt,
				(int) RecordStatusContains.ACTIVE);
		if(phieuNhaps != null && phieuNhaps.getId() != null && phieuNhaps.getId() > 0){
			phieuNhaps.setRecordStatusId(tongTien > 0 ? RecordStatusContains.ACTIVE : RecordStatusContains.DELETED_FOREVER);
			phieuNhaps.setTongTien(tongTien);
			phieuNhaps.setIsDebt(tongTien > 0);
		}else {
			phieuNhaps = new PhieuNhaps();
			phieuNhaps.setNhaCungCapMaNhaCungCap(maNhaCungCap);
			phieuNhaps.setSoPhieuNhap(0L);
			phieuNhaps.setRecordStatusId(RecordStatusContains.ACTIVE);
			phieuNhaps.setCreated(new Date());
			phieuNhaps.setCreatedByUserId(userId);
			phieuNhaps.setTongTien(tongTien);
			phieuNhaps.setIsDebt(true);
			phieuNhaps.setLoaiXuatNhapMaLoaiXuatNhap(ENoteType.InitialSupplierDebt.longValue());
			phieuNhaps.setNhaThuocMaNhaThuoc(storeCode);
			phieuNhaps.setStoreId(storeId);
			phieuNhaps.setTargetId(null);
			phieuNhaps.setTargetStoreId(null);
			phieuNhaps.setTargetManagementId(null);
			phieuNhaps.setIsModified(false);
			phieuNhaps.setConnectivityStatusID(0L);
			phieuNhaps.setDaTra(0d);
			phieuNhaps.setDiscount(0d);
			phieuNhaps.setVat(0);
		}
		phieuNhapsRepository.save(phieuNhaps);
	}
}
