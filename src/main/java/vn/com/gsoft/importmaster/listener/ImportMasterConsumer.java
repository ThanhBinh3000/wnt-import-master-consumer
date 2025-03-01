package vn.com.gsoft.importmaster.listener;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import vn.com.gsoft.importmaster.constant.ImportConstant;
import vn.com.gsoft.importmaster.entity.Process;
import vn.com.gsoft.importmaster.entity.ProcessDtl;
import vn.com.gsoft.importmaster.model.system.WrapData;
import vn.com.gsoft.importmaster.repository.ProcessDtlRepository;
import vn.com.gsoft.importmaster.repository.ProcessRepository;
import vn.com.gsoft.importmaster.service.BacSiesService;
import vn.com.gsoft.importmaster.service.KhachHangService;
import vn.com.gsoft.importmaster.service.NhaCungCapsService;
import vn.com.gsoft.importmaster.service.ThuocsService;

import java.time.*;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RefreshScope
@Slf4j
@RequiredArgsConstructor
public class ImportMasterConsumer {
    @Autowired
    private BacSiesService bacSiesService;
    @Autowired 
    private KhachHangService khachHangService;
    @Autowired
    private NhaCungCapsService nhaCungCapsService;
    @Autowired
    private ThuocsService thuocsService;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ProcessDtlRepository processDtlRepository;


    @KafkaListener(topics = "#{'${wnt.kafka.internal.consumer.topic.import-master}'}", groupId = "#{'${wnt.kafka.internal.consumer.group-id}'}", containerFactory = "kafkaInternalListenerContainerFactory")
    public void receiveExternal(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partitionId,
                                @Header(KafkaHeaders.OFFSET) Long offset,
                                @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long receivedTimestamp,
                                @Payload String payload) throws Exception {
        log.info("receivedTimestamp: {} - Received topic: {} - partition: {} - offset: {} - payload:{}", receivedTimestamp, topic, partitionId, offset, payload);
        // xử lý message
        Gson gson = new Gson();
        WrapData wrapData = gson.fromJson(payload, WrapData.class);
        Date date1 = wrapData.getSendDate(); // Thay thế bằng ngày đầu tiên
        Date date2 = new Date(); // Thay thế bằng ngày thứ hai

        // Chuyển đổi Date thành LocalDate hoặc LocalDateTime
        LocalDateTime localDateTime1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime localDateTime2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Tính khoảng thời gian sử dụng LocalDateTime
        Duration duration = Duration.between(localDateTime1, localDateTime2);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        log.info("Khoảng thời gian trong queue theo giờ, phút, giây: {} giờ {} phút {} giây", hours, minutes, seconds);
        Optional<Process> processOpt = processRepository.findByBatchKey(wrapData.getBatchKey());
        Optional<ProcessDtl> processDtlOpt = processDtlRepository.findByBatchKeyAndIndex(wrapData.getBatchKey(), wrapData.getIndex());
        if (processOpt.isPresent()) {
            processOpt.get().setStatus(1);
            processRepository.save(processOpt.get());
        }
        if (processDtlOpt.isPresent()) {
            processDtlOpt.get().setStatus(1);
            processDtlRepository.save(processDtlOpt.get());
        }
        try {
            switch (wrapData.getCode()) {
                case ImportConstant.BAC_SI:
                    bacSiesService.save(payload);
                    break;
                case ImportConstant.KHACH_HANG:
                    khachHangService.save(payload);
                    break;
                case ImportConstant.NHA_CUNG_CAP:
                    nhaCungCapsService.save(payload);
                case ImportConstant.THUOC:
                    thuocsService.save(payload);
                default:
                    log.error("Mã code chưa đuược cấu hình");
            }
            // done
            if (processDtlOpt.isPresent()) {
                processDtlOpt.get().setStatus(2);
                processDtlOpt.get().setReturnCode(0);
                processDtlOpt.get().setEndDate(new Date());
                processDtlRepository.save(processDtlOpt.get());
            }
            if (processOpt.isPresent() && processOpt.get().getReturnCode() == null) {
                processOpt.get().setReturnCode(0);
                processRepository.save(processOpt.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Xảy ra lỗi: Code {}, Index: {}, Total: {}", wrapData.getCode(), wrapData.getIndex(), wrapData.getTotal());
            // error
            if (processDtlOpt.isPresent()) {
                processDtlOpt.get().setStatus(2);
                processDtlOpt.get().setReturnCode(1);
                processDtlOpt.get().setEndDate(new Date());
                processDtlRepository.save(processDtlOpt.get());
            }
            if (processOpt.isPresent()) {
                processOpt.get().setReturnCode(1);
                processRepository.save(processOpt.get());
            }
        }finally {
            if (Objects.equals(wrapData.getTotal(), wrapData.getIndex())){
                if (processOpt.isPresent()) {
                    processOpt.get().setStatus(2);
                    processOpt.get().setEndDate(new Date());
                    processRepository.save(processOpt.get());
                }
            }
        }
    }
}