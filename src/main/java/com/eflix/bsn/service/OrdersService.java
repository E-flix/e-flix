package com.eflix.bsn.service;

import java.util.List;
import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;

public interface OrdersService {

    /* 헤더 목록 */
    List<OrdersDTO> getOrdersList();

    /* 헤더 단건 */
    OrdersDTO getOrder(String orderNo);

    /* ⭐ 디테일 목록 */
    List<OrdersDetailDTO> getOrderDetails(String orderNo);

    /* 저장(신규·수정) : 저장된 주문번호 반환 */
    String saveOrder(OrdersDTO dto);

    /* 삭제 */
    void deleteOrder(String orderNo);

    /* 주문번호 채번 */
    String generateNextOrderNo();

    /* 수정 */
    void updateOrderHeader(OrdersDTO dto);
    void updateOrderDetails(String orderNo, List<OrdersDetailDTO> details);
}
