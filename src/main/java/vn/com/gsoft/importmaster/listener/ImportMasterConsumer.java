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
import vn.com.gsoft.importmaster.model.system.WrapData;
import vn.com.gsoft.importmaster.service.BacSiesService;
import vn.com.gsoft.importmaster.service.NhaCungCapsService;

import java.time.*;
import java.util.Date;

@Service
@RefreshScope
@Slf4j
@RequiredArgsConstructor
public class ImportMasterConsumer {
    @Autowired
    private BacSiesService bacSiesService;
    @Autowired
    private NhaCungCapsService nhaCungCapsService;

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
        try {
            switch (wrapData.getCode()){
                case ImportConstant.BAC_SI:
                    bacSiesService.save(payload);
                case ImportConstant.NHA_CUNG_CAP:
                    nhaCungCapsService.save(payload);
                default:
                    log.error("Mã code chưa đuược cấu hình");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}