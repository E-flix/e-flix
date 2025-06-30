package com.eflix.bsn.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;

/**
 * 주문서 Mapper
 */
public interface OrdersMapper {
    
    List<OrdersDTO> selectAllOrders();

    //주문 헤더 insert
    void insertOrder(OrdersDTO order);

    //주문 상세 insert
    void insertOrderDetail(OrdersDetailDTO detail);

    //여신 사용액 누적
    void updateCreditUsed(
        @Param("customerCd") String customerCd,
        @Param("amount") BigDecimal amount
    );
}
