package com.eflix.bsn.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import com.eflix.bsn.mapper.OrdersMapper;
import com.eflix.bsn.service.OrdersService;
import com.eflix.common.security.auth.AuthContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 주문(ORDERS) 도메인 서비스 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private final OrdersMapper ordersMapper;
    private final AuthContext authContext;

    /*────────────────────── 조회 영역 ──────────────────────*/

    /** 헤더 목록 (헤더 그리드) */
    @Override
    @Transactional(readOnly = true)
    public List<OrdersDTO> getOrdersList() { 
        String coIdx = authContext.getCoIdx();
        return ordersMapper.selectAllOrders(coIdx);
    }

    /** 단건 헤더 + 디테일 */
    @Override
    @Transactional(readOnly = true)
    public OrdersDTO getOrder(String orderNo) {
        OrdersDTO dto = new OrdersDTO();
        dto.setCoIdx(authContext.getCoIdx());
        dto.setOrderNo(orderNo);

        OrdersDetailDTO detailDTO = new OrdersDetailDTO();
        detailDTO.setCoIdx(authContext.getCoIdx());
        detailDTO.setOrderNo(orderNo);
        OrdersDTO header = ordersMapper.selectOrderHeader(dto);        // 헤더
        if (header == null) return new OrdersDTO();                        // Not-found safety
        header.setDetails(ordersMapper.selectOrderDetails(detailDTO));       // 디테일
        return header;
    }

    /** ⭐ 디테일만 조회 (그리드 AJAX) */
    @Override
    @Transactional(readOnly = true)
    public List<OrdersDetailDTO> getOrderDetails(String orderNo) {
        OrdersDetailDTO detailDTO = new OrdersDetailDTO();
        detailDTO.setCoIdx(authContext.getCoIdx());
        detailDTO.setOrderNo(orderNo);
        return ordersMapper.selectOrderDetails(detailDTO);
    }

    /*────────────────────── 저장 영역 ──────────────────────*/

    /** 신규·수정 저장 */
    @Override
    public String saveOrder(OrdersDTO dto) {
        dto.setCoIdx(authContext.getCoIdx());
        boolean isNew = !StringUtils.hasText(dto.getOrderNo());

        /* ① 신규라면 주문번호 채번 후 헤더 INSERT */
        if (isNew) {
            dto.setOrderNo(generateNextOrderNo());
            ordersMapper.insertOrder(dto);
        }
        /* ② 수정이라면 헤더 UPDATE + 기존 디테일 삭제 */
        else {
            ordersMapper.updateOrder(dto);
            ordersMapper.deleteOrderDetailAll(dto.getOrderNo(), dto.getCoIdx());
        }

        /* ③ 디테일 라인번호 부여 후 일괄 INSERT */
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            AtomicInteger seq = new AtomicInteger(1);
            dto.getDetails().forEach(d -> {
                d.setOrderNo(dto.getOrderNo());
                d.setLineNo(seq.getAndIncrement());
            });
            ordersMapper.insertOrderDetailBatch(dto.getDetails());
        }

        return dto.getOrderNo();
    }


    /** 헤더만 업데이트 */
    @Transactional
    @Override
    public void updateOrderHeader(OrdersDTO dto) {
        ordersMapper.updateOrder(dto);
    }

     /** 단일 라인 아이템 업데이트 */
    @Transactional
    @Override
    public void updateOrderDetails(String orderNo, List<OrdersDetailDTO> details) {
         // 먼저 기존 라인 전체 삭제 후 재삽입하거나,
         // 또는 수정과 삽입을 구분하여 처리
        for (OrdersDetailDTO d : details) {
            d.setOrderNo(orderNo);
            ordersMapper.updateOrderDetail(d);
        }
    }

    /*────────────────────── 삭제 영역 ──────────────────────*/

    @Override
    @Transactional
    public void deleteOrder(String orderNo) {
        String coIdx = authContext.getCoIdx();
        // ① 디테일 먼저 삭제
        ordersMapper.deleteOrderDetailAll(orderNo, coIdx);
        // ② 헤더 삭제
        ordersMapper.deleteOrder(orderNo, coIdx);
    }

    /*────────────────── 주문번호 채번 ───────────────────*/

    @Override
    @Transactional(readOnly = true)
    public String generateNextOrderNo() {
        String coIdx = authContext.getCoIdx();
        return ordersMapper.selectNextOrderNo(coIdx);
    }
}
