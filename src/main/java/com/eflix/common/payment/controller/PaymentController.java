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

    /**
     * 결제 처리를 담당하는 컨트롤러
     * 
     * <p>
     * </p>
     * 
    * <h3>주요 기능</h3>
    * <ul>
    *   <li>상품 조회</li>
    * </ul>
    * 
    * @author 복성민 (bokseongmin@gmail.com)
    * @version 1.0
    * @since 2025-06-19
    * 
    * @see
    * 
    * @changelog
    * <ul>
    *   <li>2025-06-19: 최초 생성 (복성민)</li>
    *   <li>2025-06-20: 결제 코드 추가 (복성민)</li>
    * </ul>
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
