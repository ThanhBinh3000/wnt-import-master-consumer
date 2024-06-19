package vn.com.gsoft.importmaster.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.importmaster.entity.PhieuThuChis;
import vn.com.gsoft.importmaster.model.dto.PhieuThuChisReq;
import vn.com.gsoft.importmaster.repository.PhieuThuChisRepository;
import vn.com.gsoft.importmaster.service.PhieuThuChisService;

@Service
@Log4j2
public class PhieuThuChisServiceImpl extends BaseServiceImpl<PhieuThuChis, PhieuThuChisReq, Long> implements PhieuThuChisService {
    private PhieuThuChisRepository hdrRepo;

    @Autowired
    public PhieuThuChisServiceImpl(PhieuThuChisRepository hdrRepo) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
    }
}
