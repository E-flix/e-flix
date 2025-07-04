package com.eflix.bsn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import com.eflix.bsn.service.OrdersService;

@RestController
@RequestMapping("/bsn/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /** 주문 헤더 수정 */
    @PutMapping("/{orderNo}")
    public ResponseEntity<Void> updateOrderHeader(
            @PathVariable String orderNo,
            @RequestBody OrdersDTO dto) {
        dto.setOrderNo(orderNo);
        ordersService.updateOrderHeader(dto);
        return ResponseEntity.ok().build();
    }

    // 디테일 일괄 수정
    @PutMapping("/{orderNo}/details")
    public ResponseEntity<Void> updateOrderDetails(
        @PathVariable String orderNo,
        @RequestBody List<OrdersDetailDTO> details) {
        ordersService.updateOrderDetails(orderNo, details);
        return ResponseEntity.ok().build();
    }
}
