package vn.com.gsoft.importmaster.service.impl;


import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.importmaster.constant.GoodsTypeMap;
import vn.com.gsoft.importmaster.constant.RecordStatusContains;
import vn.com.gsoft.importmaster.constant.StatusConfirmDrugContains;
import vn.com.gsoft.importmaster.constant.TypeServiceContains;
import vn.com.gsoft.importmaster.entity.NhaCungCaps;
import vn.com.gsoft.importmaster.entity.ReplaceGoodsAndBundleGoods;
import vn.com.gsoft.importmaster.entity.Thuocs;
import vn.com.gsoft.importmaster.model.dto.ReplaceGoodsAndBundleGoodsReq;
import vn.com.gsoft.importmaster.model.dto.ThuocsReq;
import vn.com.gsoft.importmaster.model.dto.WrapData;
import vn.com.gsoft.importmaster.repository.ReplaceGoodsAndBundleGoodsRepository;
import vn.com.gsoft.importmaster.repository.ThuocsRepository;
import vn.com.gsoft.importmaster.service.ThuocsService;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@Log4j2
public class ThuocsServiceImpl extends BaseServiceImpl<Thuocs, ThuocsReq, Long> implements ThuocsService {

    private ThuocsRepository hdrRepo;
    @Autowired
    public ReplaceGoodsAndBundleGoodsRepository replaceGoodsAndBundleGoodsRepository;

    @Autowired
    public ThuocsServiceImpl(ThuocsRepository hdrRepo) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
    }


    @Override
    public Thuocs save(String payload) throws Exception {
        Gson gson = new Gson();
        TypeToken<WrapData<Thuocs>> typeToken = new TypeToken<WrapData<Thuocs>>() {};
        WrapData<Thuocs> dataThuocs = gson.fromJson(payload, typeToken.getType());

        Thuocs thuocs = dataThuocs.getData();
        var storeCode = dataThuocs.getProfile().getNhaThuoc().getMaNhaThuoc();
        String parentStoreCode = dataThuocs.getProfile().getNhaThuoc().getMaNhaThuocCha() != null && !dataThuocs.getProfile().getNhaThuoc().getMaNhaThuocCha().isEmpty()
                ? dataThuocs.getProfile().getNhaThuoc().getMaNhaThuocCha() : storeCode;
        String maThuoc = thuocs.getMaThuoc();
        String tenThuoc = thuocs.getTenThuoc();
        String barCode = thuocs.getBarCode();
        //check trùng mã
        if (!maThuoc.isEmpty()) {
            Optional<Thuocs> byMaThuocAndRecordStatusId = hdrRepo.findByMaThuoc(maThuoc, storeCode, parentStoreCode, RecordStatusContains.ACTIVE);
            if (byMaThuocAndRecordStatusId.isPresent()) {
                throw new Exception("Đã tồn tại mã " + maThuoc + ".");
            }
        }
        //check trùng tên
        if (!tenThuoc.isEmpty()) {
            Optional<Thuocs> byTenThuocAndRecordStatusId = hdrRepo.findByTenThuoc(tenThuoc, storeCode, parentStoreCode, RecordStatusContains.ACTIVE);
            if (byTenThuocAndRecordStatusId.isPresent()) {
                throw new Exception("Đã tồn tại tên " + tenThuoc + ".");
            }
        }
        //check trùng barcode
        if (!barCode.isEmpty()) {
            Optional<Thuocs> byBarcodeAndRecordStatusId = hdrRepo.findByBarCode(barCode, storeCode, parentStoreCode, RecordStatusContains.ACTIVE);
            if (byBarcodeAndRecordStatusId.isPresent()) {
                throw new Exception("Đã tồn tại mã vạch " + barCode + ".");
            }
        }
        Thuocs hdr = new Thuocs();
        BeanUtils.copyProperties(thuocs, hdr, "id");
        if (thuocs.getRecordStatusId() == null) {
            hdr.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        hdr.setStoreId(dataThuocs.getProfile().getNhaThuoc().getId());
        hdr.setNhaThuocMaNhaThuoc(dataThuocs.getProfile().getNhaThuoc().getMaNhaThuoc());
        hdr.setNhaThuocMaNhaThuocCreate(dataThuocs.getProfile().getNhaThuoc().getMaNhaThuoc());
        hdr.setCreatedByUserId(dataThuocs.getProfile().getId());
        hdr.setCreated(Date.from(Instant.now()));
        hdr.setMaThuoc(thuocs.getMaThuoc().toUpperCase());
        hdr.setCodeHash((long) thuocs.getMaThuoc().hashCode());
        hdr.setNameHash((long) thuocs.getTenThuoc().hashCode());
        hdr.setDuTru(thuocs.getGioiHan());
        hdr.setTypeService(TypeServiceContains.DRUG);
        hdr.setMetadataHash(hashCode());
        hdr.setRpMetadataHash(hdr.getRpHashCode());
        if (hdr.getDonViThuNguyenMaDonViTinh().equals(hdr.getDonViXuatLeMaDonViTinh())) {
            hdr.setDonViThuNguyenMaDonViTinh(null);
        }
        if (hdr.getGroupIdMapping() > 0) {
            hdr.setMappingDate(new Date());
            hdr.setStatusConfirm(StatusConfirmDrugContains.MAPPEDBYSYSTEM);
        } else {
            if (hdr.getFlag()) {
                hdr.setMappingDate(new Date());
                hdr.setStatusConfirm(StatusConfirmDrugContains.ADDNEW);
            }
        }

        hdr = hdrRepo.save(hdr);
        if (hdr.getId() > 0 && hdr.getFlag() && hdr.getGroupIdMapping() <= 0) {
            hdr.setGroupIdMapping(hdr.getId());
            hdr = hdrRepo.save(hdr);
        }
//        List<ReplaceGoodsAndBundleGoods> listReplaceGoods = replaceGoodsAndBundleGoodsRepository.findReplaceGoodsAndBundleGoodsByDrugStoreCodeAndAndDrugIdMap(storeCode, req.getId());
//        replaceGoodsAndBundleGoodsRepository.deleteAll(listReplaceGoods);
        saveReplaceGoodsAndBundleGoods(thuocs, hdr.getId(), storeCode);
        return hdr;
    }

    private void saveReplaceGoodsAndBundleGoods(Thuocs req, Long drugId, String storeCode){
        if(req.getReplaceGoods() != null && !req.getReplaceGoods().isEmpty()){
            for (ReplaceGoodsAndBundleGoods replaceGood : req.getReplaceGoods()) {
                ReplaceGoodsAndBundleGoods replaceGoodsData = new ReplaceGoodsAndBundleGoods();
                BeanUtils.copyProperties(replaceGood, replaceGoodsData);
                replaceGoodsData.setDrugIdMap(drugId);
                replaceGoodsData.setTypeId(GoodsTypeMap.REPLACE_GOOD);
                replaceGoodsData.setDrugStoreCode(storeCode);
                replaceGoodsAndBundleGoodsRepository.save(replaceGoodsData);
            }
        }

        if(req.getBundleGoods() != null && !req.getBundleGoods().isEmpty()){
            for (ReplaceGoodsAndBundleGoods bundleGood : req.getBundleGoods()) {
                ReplaceGoodsAndBundleGoods bundleGoodsData = new ReplaceGoodsAndBundleGoods();
                BeanUtils.copyProperties(bundleGood, bundleGoodsData);
                bundleGoodsData.setDrugIdMap(drugId);
                bundleGoodsData.setTypeId(GoodsTypeMap.BUNDLE_GOOD);
                bundleGoodsData.setDrugStoreCode(storeCode);
                replaceGoodsAndBundleGoodsRepository.save(bundleGoodsData);
            }
        }
    }
}
