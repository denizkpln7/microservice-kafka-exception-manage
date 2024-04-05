package com.denizkpln.paymentservice.service;

import com.denizkpln.paymentservice.model.Payment;
import com.denizkpln.paymentservice.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String topicName = "tjc.kafka-demo-user-created.0";

    public Payment save(Payment payment) throws ExecutionException, InterruptedException, JsonProcessingException {
        Payment savePayment=paymentRepository.save(payment);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(savePayment);
        Message<String> userMsg =
                MessageBuilder.withPayload(json)
                        .setHeader(KafkaHeaders.TOPIC, topicName)
                        // .setHeader(KafkaHeaders.KEY, user.getUserName())
                        // .setHeader(KafkaHeaders.PARTITION, 1)
                        //.setHeader("X-AgentName", "kafka-demo-app")
                        .build();


        kafkaTemplate.send(userMsg).get();
        return savePayment;
    }

}
