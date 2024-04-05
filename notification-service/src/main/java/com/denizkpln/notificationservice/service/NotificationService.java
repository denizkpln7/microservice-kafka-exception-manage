package com.denizkpln.notificationservice.service;

import com.denizkpln.notificationservice.exception.MyCustomException;
import com.denizkpln.notificationservice.model.Notification;
import com.denizkpln.notificationservice.model.Payment;

import com.denizkpln.notificationservice.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(topics = "tjc.kafka-demo-user-created.0", containerFactory = "kafkaListenerContainerFactory")
    public void consume(
            @Payload String payment,
            @Header(KafkaHeaders.OFFSET) int offset,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
    ) throws Exception {
        log.info("notification service save",payment);
        ObjectMapper objectMapper = new ObjectMapper();
        Payment dto = objectMapper.readValue(payment, Payment.class);
        handle(dto);
    }

    private void handle(Payment payment) throws MyCustomException {
        log.info("dto",payment);
        if(payment.getUserName().equals("test")){
            throw new RuntimeException("userName is null");
        }

        Notification notification=new Notification();
        notification.setEmail(payment.getUserName()+"@gmail.com");
        notificationRepository.save(notification);
    }


    @KafkaListener(topics = "tjc.kafka-demo-user-created.0.error", containerFactory = "kafkaListenerContainerFactory")
    public void consumeError(@Payload String payment) throws Exception {
        log.info("Error event received, userName: {}", payment);
        Thread.sleep(1000);
        ObjectMapper objectMapper = new ObjectMapper();
        Payment dto = objectMapper.readValue(payment, Payment.class);
        handle(dto);
    }

}
