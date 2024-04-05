package com.denizkpln.paymentservice.controller;

import com.denizkpln.paymentservice.model.Payment;
import com.denizkpln.paymentservice.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> save(@RequestBody Payment payment) throws ExecutionException, InterruptedException, JsonProcessingException {
        return new ResponseEntity<>(paymentService.save(payment),HttpStatus.OK);
    }
}
