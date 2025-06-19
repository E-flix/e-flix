package com.eflix.common.payment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.payment.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
===============================================================
 작성자     : 복성민
 작성일     : 2025-06-19
 설명       : 결제 처리 REST API
---------------------------------------------------------------
 변경 이력 :
  - 2025-06-19 (복성민): 최초 생성
===============================================================
*/

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    @PostMapping("path")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
