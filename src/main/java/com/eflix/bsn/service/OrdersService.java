package com.eflix.bsn.service;

import com.eflix.bsn.dto.OrdersDTO;
import java.util.List;

public interface OrdersService {

    /* 주문 목록 */
    List<OrdersDTO> getOrdersList();

    /* 주문 단건(헤더 + 상세) */
    OrdersDTO getOrder(String orderNo);

    /* 저장(신규 · 수정) → 저장된 주문번호 반환 */
    String saveOrder(OrdersDTO dto);

    /* 삭제 */
    void deleteOrder(String orderNo);

    /* ───────── 추가 ───────── */
    /** 신규 주문번호 채번 */
    String generateNextOrderNo();
}
